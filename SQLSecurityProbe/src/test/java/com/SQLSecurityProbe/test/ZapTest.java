package com.SQLSecurityProbe.test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.zaproxy.clientapi.core.Alert;
import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;
import org.zaproxy.clientapi.core.ApiResponseSet;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ZapTest {

	static final String ZAP_PROXY_ADDRESS = "localhost";
	static final int ZAP_PROXY_PORT = 8080;
	static final String ZAP_API_KEY = "hi1ki1dqrbighvon21l8k1dc66";
	private WebDriver driver;
	private ClientApi api;

	@BeforeMethod
	public void setup() {
		String proxyServerUrl = ZAP_PROXY_ADDRESS + ":" + ZAP_PROXY_PORT;

		Proxy proxy = new Proxy();
		proxy.setHttpProxy(proxyServerUrl);
		proxy.setSslProxy(proxyServerUrl);

		ChromeOptions co = new ChromeOptions();
		co.addArguments("--ignore-certificate-errors");
		// co.setAcceptInsecureCerts(true);
		co.setProxy(proxy);
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver(co);
		api = new ClientApi(ZAP_PROXY_ADDRESS, ZAP_PROXY_PORT, ZAP_API_KEY);
	}

	@Test
	public void amazonSecurityTest() {
	driver.get("http://localhost:3000");
		//driver.get("https://www.amazon.co.uk/");
		
		// Assert.assertTrue(driver.getTitle().contains("Amazon"));
		//Dissmiss Button
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        WebElement firstDismissButton = driver.findElement(By.xpath("//span[text()='Dismiss']"));
        firstDismissButton.click();
        
        //Account Button
        WebElement accountButton = driver.findElement(By.cssSelector("button#navbarAccount>span>span"));
        accountButton.click();
        
        //Login Button
        WebElement loginButton = driver.findElement(By.id("navbarLoginButton"));
        loginButton.click();
        
        //Email Field
        WebElement emailField = driver.findElement(By.xpath("//input[@name='email']"));
        emailField.sendKeys("admin' OR 1=1--");
        
        //Password Field
        WebElement passwordField = driver.findElement(By.xpath("//input[@name='password']"));
        passwordField.sendKeys("dkjcihidhcih");
        
        WebElement submittButton = driver.findElement(By.xpath("//button[@id='loginButton']"));
        submittButton.click();
        
        
       

	}

	@AfterMethod
	public void tearDown() {
		if (api != null) {
			String title = "OWASP Juice Shop ZAP Security Report ";
			String template = "traditional-html";
			String description = "This is OWASP Juice Shop Zap Security Test Report";
			String reportFileName = "owasp-juice-shop-zap-report.html";
			// String targetFolder = System.getProperty("report.html");
			String targetFolder = System.getProperty("user.dir") + "/OWASP ZAP Report";
			// C:\Users\angel\eclipse-workspace\SQLSecurityProbe\OWASP ZAP Report

			try {
				ApiResponse response = api.reports.generate(title, template, null, description, null, null, null, null,
						null, reportFileName, null, targetFolder, null);

				System.out.println("ZAP Report Location :" + response.toString());

				List<Alert> alerts = api.getAlerts("", -1, -1);
				for (Alert alert : alerts) {
					System.out.println("Alert Name: " + alert.getName());
					System.out.println("Risk Level: " + alert.getRisk());
					System.out.println("Description: " + alert.getDescription());
					System.out.println("Solution: " + alert.getSolution());
					// System.out.println("URI: " + alert.getUri());
					System.out.println("Parameter: " + alert.getParam());
					System.out.println("Attack: " + alert.getAttack());
					System.out.println("Evidence: " + alert.getEvidence());
					System.out.println("------------");

				}
			} catch (ClientApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		// driver.quit();
	}

}
