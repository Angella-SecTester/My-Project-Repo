package com.SQLSecurityProbe.pages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
public class HomePage {

    
    private WebDriver driver;
    String value;
     // Change this locator to the actual locator of the welcome message


   
   
   
    
    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public String getWelcomeMessage() {
     WebElement allProducts = driver.findElement(By.xpath("//div[text()='All Products']"));
    	if(allProducts.isDisplayed()) {
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    value="SUCCESS";
    	}
        return value;
    }
    
    
}
