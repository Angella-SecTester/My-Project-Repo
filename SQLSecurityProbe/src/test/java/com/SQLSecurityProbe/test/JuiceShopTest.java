package com.SQLSecurityProbe.test;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ApiResponseElement;
import org.zaproxy.clientapi.core.ClientApi;

import com.SQLSecurityProbe.base.BaseTest;
import com.SQLSecurityProbe.pages.LoginPage;
import com.SQLSecurityProbe.pages.HomePage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JuiceShopTest extends BaseTest {
    private static final String ZAP_ADDRESS = "localhost";
    private static final int ZAP_PORT = 8080;
    private static final String ZAP_API_KEY = "hi1ki1dqrbighvon21l8k1dc66";
    private String TARGET_URL = "http://localhost:3000";
    private ClientApi api;

    @BeforeMethod
    public void setUp() {
        super.setup();
        api = new ClientApi(ZAP_ADDRESS, ZAP_PORT, ZAP_API_KEY);
    }

    @AfterMethod
    public void tearDown() {
        super.tearDown();
    }

    @Test
    public void testLoginAndScan() throws Exception {
        driver.get(TARGET_URL);

        // Perform login with different payloads
        LoginPage loginPage = new LoginPage(driver);
        Thread.sleep(1000);
        loginPage.login("admin' --", "password");
        Thread.sleep(1000);
        loginPage.login("' OR '1'='1", "password");

        HomePage homePage = new HomePage(driver);
        System.out.println(homePage.getWelcomeMessage());

        // Start spidering the target
        loginPage.spiderMethod(TARGET_URL, api);

        // Start Active Scan with enhanced settings
        String target = TARGET_URL;
        System.out.println("Active scan started.");
        ApiResponse response = api.ascan.scan(target, "true", "true", null, null, null);
        String scanId = ((ApiResponseElement) response).getValue();

        // Poll the status until the scan completes
        int progress;
        do {
            Thread.sleep(5000); // Wait for 5 seconds before checking the status again
            progress = Integer.parseInt(((ApiResponseElement) api.ascan.status(scanId)).getValue());
            System.out.println("Active Scan progress: " + progress + "%");
        } while (progress < 100);

        System.out.println("Active Scan complete.");
        
        // Retrieve and print SQL Injection alerts
        retrieveAndPrintSQLInjectionAlerts(target);
    }

    private void retrieveAndPrintSQLInjectionAlerts(String target) {
        try {
            URL url = new URL("http://" + ZAP_ADDRESS + ":" + ZAP_PORT + "/JSON/alert/view/alerts/?baseurl=" + target + "&apikey=" + ZAP_API_KEY);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            connection.disconnect();

            // Parse the JSON response to count SQL Injection alerts
            String jsonResponse = content.toString();
            int sqlInjectionCount = countSQLInjectionAlerts(jsonResponse);
            System.out.println("SQL Injection Alerts: " + sqlInjectionCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int countSQLInjectionAlerts(String jsonResponse) {
        int count = 0;
        String lowerCaseResponse = jsonResponse.toLowerCase();
        int index = lowerCaseResponse.indexOf("sql injection");
        while (index != -1) {
            count++;
            lowerCaseResponse = lowerCaseResponse.substring(index + 1);
            index = lowerCaseResponse.indexOf("sql injection");
        }
        return count;
    }
}
