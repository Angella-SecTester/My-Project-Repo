package com.SQLSecurityProbe.test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class NewTest {
	WebDriver driver;

	 @BeforeClass
	    public void setup() {
	        // Set the path to the chromedriver executable
	        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
	        driver = new ChromeDriver();
	        driver.get("http://localhost:3000");  // URL of your Juice Shop instance
	    }

  @Test
  public void f() {
  }
}
