package com.SQLSecurityProbe.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import com.SQLSecurityProbe.util.ZapProxyConfig;


public class BaseTest {
	 protected WebDriver driver;

	    public void setup() {
	        // Configure the WebDriver to use ZAP proxy
	        driver = ZapProxyConfig.getDriverWithZapProxy();
	    }

	    public void tearDown() {
	        if (driver != null) {
	            driver.quit();
	        }
	    }
   }