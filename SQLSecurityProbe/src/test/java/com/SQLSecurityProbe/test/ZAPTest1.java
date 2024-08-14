
package com.SQLSecurityProbe.test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.zaproxy.clientapi.core.Alert;
import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ApiResponseElement;
import org.zaproxy.clientapi.core.ApiResponseList;
import org.zaproxy.clientapi.core.ApiResponseSet;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.List;

public class ZAPTest1 {

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
        co.setProxy(proxy);
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(co);
        api = new ClientApi(ZAP_PROXY_ADDRESS, ZAP_PROXY_PORT, ZAP_API_KEY);

        try {
            // Clear previous session data
            api.core.newSession("New Session", "true");
            // Set context to include only the target domain
            api.context.includeInContext("Default Context", "http://localhost:3000.*");
            api.context.excludeFromContext("Default Context", "http(s)?://.*"); // Exclude all non-localhost domains
        } catch (ClientApiException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void juiceShopSecurityTest() {
        driver.get("http://localhost:3000");

        try {
            Thread.sleep(3000); // Wait for page to load
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement firstDismissButton = driver.findElement(By.xpath("//span[text()='Dismiss']"));
        firstDismissButton.click();

        WebElement accountButton = driver.findElement(By.cssSelector("button#navbarAccount>span>span"));
        accountButton.click();

        WebElement loginButton = driver.findElement(By.id("navbarLoginButton"));
        loginButton.click();

        WebElement emailField = driver.findElement(By.xpath("//input[@name='email']"));
        emailField.sendKeys("admin' OR 1=1--");

        WebElement passwordField = driver.findElement(By.xpath("//input[@name='password']"));
        passwordField.sendKeys("dkjcihidhcih");

        WebElement submitButton = driver.findElement(By.xpath("//button[@id='loginButton']"));
        submitButton.click();

        try {
            // Start spidering to discover all endpoints
            api.spider.scan("http://localhost:3000", null, null, null, null);
            Thread.sleep(50000); // Adjust based on the application size

            // Get all the scanners and configure SQL Injection scanners
            ApiResponseList scannerList = (ApiResponseList) api.ascan.scanners("Default Policy", null);
            for (ApiResponse scanner : scannerList.getItems()) {
                ApiResponseSet scannerSet = (ApiResponseSet) scanner;
                String scannerId = scannerSet.getStringValue("id");
                String scannerName = scannerSet.getStringValue("name");
                
                // Enable SQL Injection related scanners
                if (scannerName.contains("SQL Injection")) {
                    api.ascan.enableScanners(scannerId, scannerName);
                    api.ascan.setScannerAlertThreshold(scannerId, "DEFAULT", scannerName);
                    api.ascan.setScannerAttackStrength(scannerId, "HIGH", scannerName);
                }
            }

            // Perform the scan
            api.ascan.scan("http://localhost:3000", null, null, null, null, null);
            Thread.sleep(60000); // Adjust based on scan duration to ensure thorough scanning
        } catch (ClientApiException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterMethod
    public void tearDown() {
        if (api != null) {
            String title = "OWASP Juice Shop ZAP Security Report ";
            String template = "traditional-html";
            String description = "This is OWASP Juice Shop Zap Security Test Report";
            String reportFileName = "owasp-juice-shop-zap-report.html";
            String targetFolder = System.getProperty("user.dir") + "/OWASP ZAP Report";

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
                    System.out.println("Parameter: " + alert.getParam());
                    System.out.println("Attack: " + alert.getAttack());
                    System.out.println("Evidence: " + alert.getEvidence());
                    System.out.println("------------");
                }
            } catch (ClientApiException e) {
                e.printStackTrace();
            }
        }
        driver.quit();
    }
}
