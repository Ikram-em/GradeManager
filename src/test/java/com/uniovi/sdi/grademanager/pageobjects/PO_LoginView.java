package com.uniovi.sdi.grademanager.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_LoginView extends PO_NavView {

    public static void goToLoginForm(WebDriver driver) {
        clickOption(driver, "login", "class", "btn btn-primary");
    }

    public static void fillLoginForm(WebDriver driver, String dnip, String passwordp) {
        WebElement dni = driver.findElement(By.name("username"));
        dni.click();
        dni.clear();
        dni.sendKeys(dnip);

        WebElement password = driver.findElement(By.name("password"));
        password.click();
        password.clear();
        password.sendKeys(passwordp);

        By boton = By.cssSelector("form button[type='submit']");
        driver.findElement(boton).click();
    }



    public static void login(WebDriver driver, String dni, String password, String expectedText) {
        goToLoginForm(driver);
        fillLoginForm(driver, dni, password);
        checkElementBy(driver, "text", expectedText);
    }
}
