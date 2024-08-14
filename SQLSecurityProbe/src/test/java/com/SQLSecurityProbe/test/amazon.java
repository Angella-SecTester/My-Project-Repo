package com.SQLSecurityProbe.test;

import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.openqa.selenium.Alert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
public class amazon {
	  public static void main(String[] args) {
		  try {
	            // Set the path to the chromedriver executable
	            System.setProperty("webdriver.chrome.driver", "ChromeDriver/chromedriver.exe");

	            // Initialize ChromeOptions
	            /*ChromeOptions options = new ChromeOptions();
	            // Add options if necessary, e.g., options.addArguments("--headless");
	            options.addArguments("--disable-web-security"); // Disable web security
	            options.addArguments("--allow-running-insecure-content"); // Allow running insecure content
	            options.addArguments("--no-sandbox"); // Bypass OS security model, necessary if running as root on Linux
	            options.addArguments("--disable-dev-shm-usage"); // Overcome limited resource problems
	            options.addArguments("--remote-allow-origins=*"); // Allow remote origins*/


	            // Create a new instance of ChromeDriver
	            WebDriver driver = new ChromeDriver();

	            // Open Amazon's homepage
	            driver.get("http://localhost:3000");
	            Thread.sleep(1000);
	            
	            //Alert alert = driver.switchTo().alert(); // switch to alert

	            //String alertMessage= driver.switchTo().alert().getText(); // capture alert message
	            
	            //WebElement loginButton = driver.findElement(By.id("navbarLoginButton"));
	            //loginButton.click();

	            WebElement firstDismissButton = driver.findElement(By.xpath("//span[text()='Dismiss']"));
	            firstDismissButton.click();
	           // WebElement firstDismissButton = driver.findElement(By.xpath("//span[text()='Dismiss']"));
	            //firstDismissButton.click();
	            WebElement emailField = driver.findElement(By.id("email"));
	            WebElement passwordField = driver.findElement(By.id("password"));
	            WebElement submitButton = driver.findElement(By.id("loginButton"));

	            // Print the title of the page
	            System.out.println("Page title is: " + driver.getTitle());

	            // Close the browser window
	            // driver.quit();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	       
	    }
}
