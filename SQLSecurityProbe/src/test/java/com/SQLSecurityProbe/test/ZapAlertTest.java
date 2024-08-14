package com.SQLSecurityProbe.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

public class ZapAlertTest {
	public static void main(String[] args) {
        String filePath = "zap_alerts_results.txt"; // Replace with your file path
        
        Set<String> uniqueAlerts = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("Alert: ")) {
                    // Extract the alert name
                    String alertName = line.substring(line.indexOf("Alert: ") + 7, line.indexOf(", Risk:")).trim();
                    // Add the alert name to the set
                    uniqueAlerts.add(alertName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Output the number of unique alerts
        System.out.println("Total number of unique alerts: " + uniqueAlerts.size());
    }
}
/*public static void main(String[] args) {
    String filePath = "zap_alerts_results.txt"; // Replace with your file path
    
    // Maps to store counts by risk level and by alert type
    Map<String, Integer> alertSummary = new HashMap<>();
    alertSummary.put("High", 0);
    alertSummary.put("Medium", 0);
    alertSummary.put("Low", 0);
    alertSummary.put("Informational", 0);

    Map<String, Integer> alertDetails = new HashMap<>();
    Map<String, String> alertRiskLevels = new HashMap<>();

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = br.readLine()) != null) {
            if (line.contains("Alert: ") && line.contains("Risk: ")) {
                // Extract alert name
                String alertName = line.substring(line.indexOf("Alert: ") + 7, line.indexOf(", Risk:")).trim();
                
                // Extract risk level
                String riskLevel = line.substring(line.indexOf("Risk: ") + 6, line.indexOf(", Confidence:")).trim();
                
                // Update the summary count by risk level
                alertSummary.put(riskLevel, alertSummary.get(riskLevel) + 1);
                
                // Update the detailed alert count and risk level
                alertDetails.put(alertName, alertDetails.getOrDefault(alertName, 0) + 1);
                alertRiskLevels.put(alertName, riskLevel);
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    // Output the summary similar to the one in the image
    System.out.println("Summary of Alerts");
    System.out.println("Risk Level    Number of Alerts");
    System.out.printf("High          %d%n", alertSummary.get("High"));
    System.out.printf("Medium        %d%n", alertSummary.get("Medium"));
    System.out.printf("Low           %d%n", alertSummary.get("Low"));
    System.out.printf("Informational %d%n", alertSummary.get("Informational"));

    // Output detailed alert count
    System.out.println("\nAlerts");
    System.out.println("Name                                      Risk Level   Number of Instances");
    for (Map.Entry<String, Integer> entry : alertDetails.entrySet()) {
        String alertName = entry.getKey();
        int count = entry.getValue();
        String riskLevel = alertRiskLevels.get(alertName);
        System.out.printf("%-40s %-12s %d%n", alertName, riskLevel, count);
    }
}*/