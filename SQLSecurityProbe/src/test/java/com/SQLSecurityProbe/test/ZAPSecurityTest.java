package com.SQLSecurityProbe.test;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;
import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ApiResponseElement;
import org.zaproxy.clientapi.core.ApiResponseSet;
import org.zaproxy.clientapi.core.ApiResponseList;

public class ZAPSecurityTest {

    private static final String JUICE_SHOP_URL = "http://localhost:3000";
    private static final String ZAP_PROXY_URL = "https://localhost:8080";
    private static final String ZAP_API_KEY = "hi1ki1dqrbighvon21l8k1dc66"; // Set your ZAP API key if necessary

    public static void main(String[] args) {
        try {
            ClientApi api = new ClientApi("localhost", 8080, ZAP_API_KEY);

            // Start ZAP Spider
            System.out.println("Starting ZAP spider...");
            String spiderScanId = startSpider(api);
            waitForSpiderToFinish(api, spiderScanId);

            // Start ZAP Active Scan
            System.out.println("Starting ZAP active scan...");
            String activeScanId = startActiveScan(api);
            waitForActiveScanToFinish(api, activeScanId);

            // Retrieve and print alerts
            System.out.println("Retrieving alerts...");
            retrieveAlerts(api);

        } catch (ClientApiException e) {
            e.printStackTrace();
        }
    }

    private static String startSpider(ClientApi api) throws ClientApiException {
        ApiResponse response = api.spider.scan(JUICE_SHOP_URL, null, null, null, null);
        return ((ApiResponseElement) response).getValue();
    }

    private static String startActiveScan(ClientApi api) throws ClientApiException {
        // Corrected method signature
        ApiResponse response = api.ascan.scan(JUICE_SHOP_URL, "true", null, null, null, null);
        return ((ApiResponseElement) response).getValue();
    }

    private static void waitForSpiderToFinish(ClientApi api, String scanId) throws ClientApiException {
        while (true) {
            String status = api.spider.status(scanId).toString();
            System.out.println("Spider progress: " + status + "%");
            if ("100".equals(status)) {
                break;
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Spider completed.");
    }

    private static void waitForActiveScanToFinish(ClientApi api, String scanId) throws ClientApiException {
        while (true) {
            String status = api.ascan.status(scanId).toString();
            System.out.println("Active scan progress: " + status + "%");
            if ("100".equals(status)) {
                break;
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Active scan completed.");
    }

    private static void retrieveAlerts(ClientApi api) throws ClientApiException {
        // Adjusted method call for latest API
        ApiResponse response = api.core.alerts(JUICE_SHOP_URL, "0", "1000"); // Ensure correct parameter types

        if (response instanceof ApiResponseList) {
            ApiResponseList alerts = (ApiResponseList) response;
            for (ApiResponse alert : alerts.getItems()) {
                if (alert instanceof ApiResponseElement) {
                    ApiResponseElement alertElement = (ApiResponseElement) alert;
                    System.out.println("Alert: " + alertElement.getValue());
                }
            }
        }
    }
}