package com.example.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class CustomDriverManager {
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        if (driver.get() == null) {
            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();
            // Remove --user-data-dir to avoid profile locking in CI

            // Add recommended flags for Linux CI environments
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--headless=new");  // Use latest headless mode
            options.addArguments("--disable-gpu");   // Disable GPU acceleration

            // Print Chrome options for debug
            System.out.println("Starting Chrome with options: " + options.asMap());

            WebDriver drv = new ChromeDriver(options);
            drv.manage().window().maximize();

            driver.set(drv);
        }
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}
