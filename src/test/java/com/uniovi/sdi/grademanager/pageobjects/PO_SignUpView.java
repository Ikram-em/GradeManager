package com.uniovi.sdi.grademanager.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PO_SignUpView extends PO_NavView {

    public static void fillForm(WebDriver driver, String dni, String name, String lastName, String password, String confirmPassword) {
        driver.findElement(By.xpath("//input[@name='dni']")).clear();
        driver.findElement(By.xpath("//input[@name='dni']")).sendKeys(dni);
        driver.findElement(By.xpath("//input[@name='name']")).clear();
        driver.findElement(By.xpath("//input[@name='name']")).sendKeys(name);
        driver.findElement(By.xpath("//input[@name='lastName']")).clear();
        driver.findElement(By.xpath("//input[@name='lastName']")).sendKeys(lastName);
        driver.findElement(By.xpath("//input[@name='password']")).clear();
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys(password);
        driver.findElement(By.xpath("//input[@name='passwordConfirm']")).clear();
        driver.findElement(By.xpath("//input[@name='passwordConfirm']")).sendKeys(confirmPassword);
    }

    public static void submit(WebDriver driver) {
        checkElementBy(driver, "free", "//button[@type='submit']").get(0).click();
    }
}
