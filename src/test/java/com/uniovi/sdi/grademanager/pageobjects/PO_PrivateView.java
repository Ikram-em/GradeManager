package com.uniovi.sdi.grademanager.pageobjects;

import com.uniovi.sdi.grademanager.util.SeleniumUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class PO_PrivateView extends PO_NavView {

    public static void fillFormAddMark(WebDriver driver, int userOrder, String descriptionp, String scorep) {
        SeleniumUtils.waitSeconds(driver, 5);
        new Select(driver.findElement(By.id("user"))).selectByIndex(userOrder);

        WebElement description = driver.findElement(By.name("description"));
        description.clear();
        description.sendKeys(descriptionp);

        WebElement score = driver.findElement(By.name("score"));
        score.click();
        score.clear();
        score.sendKeys(scorep);

        By boton = By.className("btn");
        driver.findElement(boton).click();
    }
}
