package com.SQLSecurityProbe.test;
import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ApiResponseElement;
import org.zaproxy.clientapi.core.ApiResponseList;
import org.zaproxy.clientapi.core.ApiResponseSet;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;

public class ZapScopeConfiguration {
    private static final String ZAP_PROXY_ADDRESS = "localhost";
    private static final int ZAP_PROXY_PORT = 8080;
    private static final String ZAP_API_KEY = "hi1ki1dqrbighvon21l8k1dc66";  // Replace with your ZAP API key
    private static final String CONTEXT_NAME = "JuiceShopContext";

    public static void main(String[] args) {
        ClientApi api = new ClientApi(ZAP_PROXY_ADDRESS, ZAP_PROXY_PORT, ZAP_API_KEY);
        String contextId = null;

        try {
            // Check if context already exists
            ApiResponse response = api.context.contextList();
            if (response instanceof ApiResponseList) {
                ApiResponseList responseList = (ApiResponseList) response;
                for (ApiResponse item : responseList.getItems()) {
                    if (item instanceof ApiResponseElement) {
                        ApiResponseElement element = (ApiResponseElement) item;
                        if (element.getValue().equals(CONTEXT_NAME)) {
                            contextId = element.getValue();
                            break;
                        }
                    }
                }
            }

            if (contextId == null) {
                // Create a new context if it doesn't exist
                ApiResponse newContextResponse = api.context.newContext(CONTEXT_NAME);
                if (newContextResponse instanceof ApiResponseElement) {
                    contextId = ((ApiResponseElement) newContextResponse).getValue();
                }
            }

            // Include in context: Adding Juice Shop URL
            String includeUrlPattern = "http://localhost:3000";  // Adjust this pattern as needed
            api.context.includeInContext(CONTEXT_NAME, includeUrlPattern);

            // Optionally exclude specific URLs or patterns
            // String excludeUrlPattern = "http://some-external-site.com.*";
            // api.context.excludeFromContext(CONTEXT_NAME, excludeUrlPattern);

            // Check context settings
            ApiResponse contextResponse = api.context.context(contextId);
            System.out.println("Context details: " + contextResponse.toString());

        } catch (ClientApiException e) {
            e.printStackTrace();
        }
    }
}