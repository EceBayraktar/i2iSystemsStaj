package com.example;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://www.saucedemo.com");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void login(String username, String password) {
        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));
        WebElement passwordInput = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login-button"));

        usernameInput.clear();
        usernameInput.sendKeys(username);
        passwordInput.clear();
        passwordInput.sendKeys(password);
        loginButton.click();
    }

    @Test
    @DisplayName("Başarılı Login Testi")
    public void testSuccessfulLogin() {
        login("standard_user", "secret_sauce");

        // URL kontrolü
        assertTrue(driver.getCurrentUrl().contains("inventory"),
                "Giriş sonrası URL 'inventory' içermeli.");

        // Ürün listesinin görünür olduğunu kontrol et
        WebElement inventoryContainer = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("inventory_container")));
        assertTrue(inventoryContainer.isDisplayed(), "Ürün listesi görünür olmalı.");
    }

    @Test
    @DisplayName("Hatalı Login Testi")
    public void testFailedLogin() {
        login("wrong_user", "wrong_password");

        // Hata mesajı görünmeli
        WebElement errorMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3[data-test='error']")));
        assertTrue(errorMessage.isDisplayed(), "Hata mesajı görünür olmalı.");

        // Hata mesaj içeriği kontrolü
        assertEquals("Epic sadface: Username and password do not match any user in this service",
                errorMessage.getText().trim());
    }
    @Test
    @DisplayName("Boş Alanlarla Login Testi")
    public void testEmptyFieldsLogin() {
        login("", "");
        WebElement errorMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3[data-test='error']")));
        assertTrue(errorMessage.isDisplayed());
        assertEquals("Epic sadface: Username is required", errorMessage.getText().trim());
    }
    @Test
    @DisplayName("Doğru Kullanıcı Adı, Yanlış Şifre ile Login Testi")
    public void testCorrectUsernameWrongPassword() {
        login("standard_user", "wrong_password");

        WebElement errorMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3[data-test='error']")));
        assertTrue(errorMessage.isDisplayed());
        assertEquals("Epic sadface: Username and password do not match any user in this service",
                errorMessage.getText().trim());
    }
    @Test
    @DisplayName("Yanlış Kullanıcı Adı, Doğru Şifre ile Login Testi")
    public void testWrongUsernameCorrectPassword() {
        login("wrong_user", "secret_sauce");    
        WebElement errorMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3[data-test='error']")));
        assertTrue(errorMessage.isDisplayed());
        assertEquals("Epic sadface: Username and password do not match any user in this service",
                errorMessage.getText().trim()); 
    }

}
