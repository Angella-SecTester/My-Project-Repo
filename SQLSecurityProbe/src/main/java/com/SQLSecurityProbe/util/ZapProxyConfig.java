package com.SQLSecurityProbe.util;

import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.Proxy;

public class ZapProxyConfig {
	public static WebDriver getDriverWithZapProxy() {
        String zapProxy = "localhost:8080"; // Default ZAP proxy port

        Proxy proxy = new Proxy();
        proxy.setHttpProxy(zapProxy).setFtpProxy(zapProxy).setSslProxy(zapProxy);

        ChromeOptions options = new ChromeOptions();
        options.setProxy(proxy);

        // Set the ChromeDriver path
       // System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        System.setProperty("webdriver.chrome.driver", "ChromeDriver/chromedriver.exe");
        // Initialize the WebDriver
        return new ChromeDriver(options);
    }
}
