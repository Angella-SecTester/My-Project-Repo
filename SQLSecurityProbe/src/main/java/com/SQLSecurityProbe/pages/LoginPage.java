package com.SQLSecurityProbe.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class LoginPage {
	private WebDriver driver;

	public LoginPage(WebDriver driver) {
		this.driver = driver;
	}

	public void login(String email, String password) throws InterruptedException {
		
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		
		// Wait until the element with the specified XPath is visible
		WebElement dismissButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Dismiss']")));
		if (dismissButton.isDisplayed()) {
			dismissButton.click();
		}

		WebElement accountButton = driver.findElement(By.cssSelector("button#navbarAccount>span>span"));
		accountButton.click();

		WebElement loginButton = driver.findElement(By.id("navbarLoginButton"));
		loginButton.click();

		WebElement emailField = driver.findElement(By.xpath("//input[@name='email']"));
		emailField.sendKeys(email);

		WebElement passwordField = driver.findElement(By.xpath("//input[@name='password']"));
		passwordField.sendKeys(password);

		WebElement submitButton = driver.findElement(By.xpath("//button[@id='loginButton']"));
		submitButton.click();
	}

	public void spiderMethod(String TARGET_URL, ClientApi api) throws ClientApiException, Exception {
		// String hjh;
		// Start spidering the target URL
		System.out.println("Starting spider...");
		api.spider.scan(TARGET_URL, null, null, null, null);

		// Wait for the spider to complete
		while (true) {
			Thread.sleep(1000);
			int progress = Integer.parseInt(api.spider.status(TARGET_URL).toString(2).replaceAll("[^0-9]", ""));
			System.out.println("Spider progress: " + progress + "%");
			if (progress >= 100) {
				// hjh = progress + "";
				break;
			}
		}

		System.out.println("Spider completed.");

	}
}
