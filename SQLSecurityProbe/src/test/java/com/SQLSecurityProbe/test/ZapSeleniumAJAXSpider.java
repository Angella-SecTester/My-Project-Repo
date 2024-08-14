package com.SQLSecurityProbe.test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ApiResponseElement;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;

import java.util.HashMap;
import java.util.Map;

public class ZapSeleniumAJAXSpider {
    private static final String ZAP_ADDRESS = "localhost";
    private static final int ZAP_PORT = 8080;
    private static final String ZAP_API_KEY = "hi1ki1dqrbighvon21l8k1dc66"; // Get it from ZAP if enabled
    private static final String TARGET = "http://localhost:3000"; // URL of your Juice Shop

    public static void main(String[] args) throws InterruptedException {
        // Setup Selenium to use ZAP proxy
        System.setProperty("webdriver.chrome.driver", "ChromeDriver/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--proxy-server=http://" + ZAP_ADDRESS + ":" + ZAP_PORT);
        WebDriver driver = new ChromeDriver(options);

        // Initialize ZAP client
        ClientApi api = new ClientApi(ZAP_ADDRESS, ZAP_PORT, ZAP_API_KEY);

        try {
            // Navigate to the target website
            driver.get(TARGET);
            Thread.sleep(5000); // Wait for the page to load

            // Check if AJAX spider is already running
            String ajaxSpiderStatus = ((ApiResponseElement) api.ajaxSpider.status()).getValue();
            if ("stopped".equalsIgnoreCase(ajaxSpiderStatus)) {
                // Start AJAX spidering with Chrome
                System.out.println("Starting AJAX spider...");
                ApiResponse response = startAjaxSpider(api, TARGET);
                String scanID = ((ApiResponseElement) response).getValue();

                // Poll the status of the AJAX spider until it completes
                String status;
                do {
                    Thread.sleep(1000);
                    status = ((ApiResponseElement) api.ajaxSpider.status()).getValue();
                    System.out.println("AJAX Spider status: " + status);
                } while (!"stopped".equalsIgnoreCase(status));

                System.out.println("AJAX Spider completed.");
            } else {
                System.out.println("AJAX spider is already running. Please wait until it completes.");
            }

        } catch (ClientApiException e) {
            e.printStackTrace();
            System.err.println("Error starting AJAX Spider: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }

    private static ApiResponse startAjaxSpider(ClientApi api, String target) throws ClientApiException {
        Map<String, String> map = new HashMap<>();
        map.put("url", target);
        map.put("inScope", "true");
        map.put("contextName", "");
        map.put("subtreeOnly", "false");
        map.put("browserId", "chrome"); // Explicitly set to Chrome
        return api.callApi("ajaxSpider", "action", "scan", map);
    }
}
