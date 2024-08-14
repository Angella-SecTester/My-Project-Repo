package com.SQLSecurityProbe.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ZapAlertUtils {

    // Inner class to represent an Alert
    public static class Alert {
        String alertType;
        String risk;
        String confidence;
        String url;
        String param;
        String other;

        @Override
        public String toString() {
            return "Alert: " + alertType + "\n" +
                    "Risk: " + risk + "\n" +
                    "Confidence: " + confidence + "\n" +
                    "URL: " + url + "\n" +
                    "Parameter: " + param + "\n" +
                    "Details: " + other + "\n";
        }
    }

    // Method to parse and print alerts from a ZAP alerts file
    public static void parseAndPrintZapAlerts(String filePath) {
        List<Alert> alerts = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            Alert currentAlert = null;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("Alert:")) {
                    if (currentAlert != null) {
                        alerts.add(currentAlert);
                    }
                    currentAlert = new Alert();
                    currentAlert.alertType = line.substring(7).trim();
                } else if (line.startsWith("Risk:")) {
                    if (currentAlert != null) {
                        currentAlert.risk = line.substring(6).trim();
                    }
                } else if (line.startsWith("Confidence:")) {
                    if (currentAlert != null) {
                        currentAlert.confidence = line.substring(12).trim();
                    }
                } else if (line.startsWith("Url:")) {
                    if (currentAlert != null) {
                        currentAlert.url = line.substring(5).trim();
                    }
                } else if (line.startsWith("Param:")) {
                    if (currentAlert != null) {
                        currentAlert.param = line.substring(7).trim();
                    }
                } else if (line.startsWith("Other:")) {
                    if (currentAlert != null) {
                        currentAlert.other = line.substring(7).trim();
                    }
                }
            }
            // Add the last alert if present
            if (currentAlert != null) {
                alerts.add(currentAlert);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Print the parsed alerts
        printAlerts(alerts);
    }

    // Helper method to print the alerts and summarize them
    private static void printAlerts(List<Alert> alerts) {
        System.out.println("Total Alerts Found: " + alerts.size());

        int highRiskCount = 0;
        int mediumRiskCount = 0;
        int lowRiskCount = 0;
        int informationalRiskCount = 0;

        for (Alert alert : alerts) {
            switch (alert.risk.toLowerCase()) {
                case "High":
                    highRiskCount++;
                    break;
                case "Medium":
                    mediumRiskCount++;
                    break;
                case "Low":
                    lowRiskCount++;
                    break;
                case "Informational":
                    informationalRiskCount++;
                    break;
            }

            System.out.println(alert);
        }

        // Summary
        System.out.println("Summary:");
        System.out.println("High Risk Alerts: " + highRiskCount);
        System.out.println("Medium Risk Alerts: " + mediumRiskCount);
        System.out.println("Low Risk Alerts: " + lowRiskCount);
        System.out.println("Informational Alerts: " + informationalRiskCount);
    }
}