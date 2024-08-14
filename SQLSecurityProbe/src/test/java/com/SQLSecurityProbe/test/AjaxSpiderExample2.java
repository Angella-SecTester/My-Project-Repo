package com.SQLSecurityProbe.test;

import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ApiResponseElement;
import org.zaproxy.clientapi.core.ApiResponseList;
import org.zaproxy.clientapi.core.ApiResponseSet;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;

public class AjaxSpiderExample2 {
	private static final String ZAP_ADDRESS = "localhost";
	private static final int ZAP_PORT = 8080;
	private static final String ZAP_API_KEY = "hi1ki1dqrbighvon21l8k1dc66";
	private static final String TARGET = "http://localhost:3000"; // Ensure this is the correct target URL

	public static void main(String[] args) {
		ClientApi api = new ClientApi(ZAP_ADDRESS, ZAP_PORT, ZAP_API_KEY);

		try {
			// Log connection details
			System.out.println("Connecting to ZAP API at " + ZAP_ADDRESS + ":" + ZAP_PORT);

			// Test API connectivity
			System.out.println("Testing API connectivity...");
			api.core.version();
			System.out.println(api.core.version());

			System.out.println("Successfully connected to ZAP API.");

			// Set the browser to Chrome
			api.ajaxSpider.setOptionBrowserId("chrome");

			String status;
			status = api.ajaxSpider.status().toString(2);
			System.out.println("AJAX Spider status: " + status);

			// Start AJAX Spider scan
			System.out.println("Starting AJAX Spider scan for " + TARGET);
			api.ajaxSpider.scan(TARGET, null, null, null); // Start the scan

			// Poll the status until it is completed

			do {
				status = api.ajaxSpider.status().toString(2);
				System.out.println("AJAX Spider status: " + status);
				// Thread.sleep(5000);
			} while ("running".equalsIgnoreCase(api.ajaxSpider.status().toString(2)));
			Thread.sleep(60000);

			// Get the number of crawled URLs by splitting the results string
			String resulrts = api.ajaxSpider.results("0", "10000").toString(2);
			int numberOfCrawledUrls = resulrts.split("\"url\":").length - 1;
			System.out.println("Number of crawled URLs so far: " + numberOfCrawledUrls);

			// System.out.println(results);
			System.out.println("AJAX Spider status: " + status);

			// Optionally, save the results to a file
			java.nio.file.Files.write(java.nio.file.Paths.get("ajax_spider_results.txt"), resulrts.getBytes());

		} catch (ClientApiException e) {
			System.err.println("ClientApiException occurred: " + e.getMessage());
			e.printStackTrace();
		} catch (java.io.IOException e) {
			System.err.println("IOException occurred: " + e.getMessage());
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
