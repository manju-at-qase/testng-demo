package com.example.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

public class CustomDriverManager {
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        if (driver.get() == null) {
            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();

            // Create a temporary user-data-dir for isolated session, which prevents conflicts
            try {
                Path tempProfileDir = Files.createTempDirectory("chrome-profile-");
                options.addArguments("--user-data-dir=" + tempProfileDir.toAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                // fallback: do not set user-data-dir if temp folder creation fails; less ideal
            }

            // Recommended flags for Linux CI environments
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");

            // Headless optional: uncomment if UI output isn't necessary
            // options.addArguments("--headless=new");

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
