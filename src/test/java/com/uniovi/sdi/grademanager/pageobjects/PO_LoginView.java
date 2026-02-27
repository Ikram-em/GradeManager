package com.uniovi.sdi.grademanager.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PO_LoginView extends PO_NavView {

    public static void fillForm(WebDriver driver, String dni, String password) {
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys(dni);
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(password);
    }

    public static void submit(WebDriver driver) {
        checkElementBy(driver, "free", "//button[@type='submit']").get(0).click();
    }

    public static void login(WebDriver driver, String dni, String password) {
        fillForm(driver, dni, password);
        submit(driver);
    }
}
