package com.SQLSecurityProbe.test;
//Import necessary packages
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ApiResponseElement;
import org.zaproxy.clientapi.core.ApiResponseList;
import org.zaproxy.clientapi.core.ClientApi;

import com.SQLSecurityProbe.pages.LoginPage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import io.github.bonigarcia.wdm.WebDriverManager;



//Your test class
public class SqlInjectionTest {
 WebDriver driver;
 LoginPage loginPage;
 private static final String OUTPUT_FILE = "zap_ajspider_results.txt";

 @BeforeClass
 public void setUp() {
     // Setup ZAP proxy
     Proxy proxy = new Proxy();
     proxy.setHttpProxy("localhost:8080"); // Replace with your ZAP proxy port if different
     proxy.setSslProxy("localhost:8080");

     ChromeOptions options = new ChromeOptions();
     options.setProxy(proxy);

     WebDriverManager.chromedriver().setup();
     driver = new ChromeDriver(options);
     loginPage = new LoginPage(driver);
     driver.get("http://localhost:3000"); // Assuming Juice Shop is running locally on port 3000
 }

 @Test
 public void sqlInjectionTest() throws InterruptedException {
     // Perform SQL Injection
     loginPage.login("' OR 1=1 --", "password");

     // Trigger ZAP Scan
     triggerZapScan();
 }

 @AfterClass
 public void tearDown() {
     if (driver != null) {
         driver.quit();
     }
 }

 public void triggerZapScan() {
     String zapAddress = "localhost";
     int zapPort = 8080;
     String zapApiKey = "hi1ki1dqrbighvon21l8k1dc66"; // Set this if you have an API key enabled in ZAP
     String targetUrl = "http://localhost:3000";

     ClientApi api = new ClientApi(zapAddress, zapPort, zapApiKey);

     try {
         // Start spider scan
    	  ApiResponse scanId = api.spider.scan(targetUrl, null, null, null, null);
         String scanID = scanId.toString();
         int progress;
         do {
             Thread.sleep(1000);
             progress = Integer.parseInt(((ApiResponseElement) api.spider.status(scanID)).getValue());
             System.out.println("Spider progress : " + progress + "%");
         } while (progress < 100);
         System.out.println("Spider completed");
         

			// Step 5: Start the AJAX Spider
			System.out.println("Starting AJAX Spider on " + targetUrl);
			api.ajaxSpider.scan(targetUrl, null, null, null);

			// Step 6: Wait for the AJAX Spider to complete
			while (true) {
				ApiResponse statusResponse = api.ajaxSpider.status();
				String status = ((ApiResponseElement) statusResponse).getValue();
				System.out.println("AJAX Spider status: " + status);
				if ("stopped".equalsIgnoreCase(status)) {
					break;
				}
				Thread.sleep(5000); // Wait for 5 seconds before checking again
			}

			System.out.println("AJAX Spider completed.");

			// Retrieve the list of crawled URLs
			ApiResponseList crawledUrlsResponse = (ApiResponseList) api.ajaxSpider.results(null, null);
			List<ApiResponse> urlsList = crawledUrlsResponse.getItems();

			// Print the number of URLs crawled
			System.out.println("Number of URLs crawled: " + urlsList.size());

			// Convert the results to a JSON formatted string
			String jsonResults = crawledUrlsResponse.toString(2);

			// Save the results to a file (overwrite if it exists)
			saveResultsToFile("zap_ajspider_results.txt", jsonResults);


         // Start active scan
         scanId = api.ascan.scan(targetUrl, "True", "False", null, null, null);
         do {
             Thread.sleep(5000);
             progress = Integer.parseInt(((ApiResponseElement) api.ascan.status(scanID)).getValue());
             System.out.println("Scan progress : " + progress + "%");
         } while (progress < 100);
         System.out.println("Scan completed");

         // Retrieve the alerts
         ApiResponseList alerts = (ApiResponseList) api.core.alerts(targetUrl, "0", "10");
         for (ApiResponse alert : alerts.getItems()) {
             System.out.println(alert.toString());
         }
      // Generate and save the report
         byte[] report = api.core.htmlreport();
         try (FileOutputStream fos = new FileOutputStream("zap-report.html")) {
             fos.write(report);
         } catch (IOException e) {
             e.printStackTrace();
         }
         
         

     } catch (Exception e) {
         e.printStackTrace();
     }
 }


//Method to save results to a file, overwriting if it exists
	private static void saveResultsToFile(String filePath, String content) {
		File file = new File(filePath);
		try (PrintWriter writer = new PrintWriter(new FileWriter(file, false))) {
			writer.write(content);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}