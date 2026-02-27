package com.uniovi.sdi.grademanager.pageobjects;

import com.uniovi.sdi.grademanager.util.SeleniumUtils;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class PO_PrivateView extends PO_NavView {

    public static void goToMarks(WebDriver driver) {
        driver.navigate().to("http://localhost:8090/mark/list");
    }

    public static void searchMark(WebDriver driver, String text) {
        WebElement search = driver.findElement(By.name("searchText"));
        search.clear();
        search.sendKeys(text);
        checkElementBy(driver, "free", "//form[@action='/mark/list']//button[@type='submit']").get(0).click();
    }

    public static void openFirstDetails(WebDriver driver) {
        checkElementBy(driver, "free", "//table[@id='marksTable']//a[contains(@href,'/mark/details/')]").get(0).click();
    }

    public static boolean hasDetailsLink(WebDriver driver) {
        return !driver.findElements(By.xpath("//table[@id='marksTable']//a[contains(@href,'/mark/details/')]")).isEmpty();
    }

    public static void goToAddMark(WebDriver driver) {
        driver.navigate().to("http://localhost:8090/mark/add");
    }

    public static void addMark(WebDriver driver, String description, String score) {
        driver.findElement(By.id("description")).clear();
        driver.findElement(By.id("description")).sendKeys(description);
        driver.findElement(By.id("score")).clear();
        driver.findElement(By.id("score")).sendKeys(score);

        Select userSelect = new Select(driver.findElement(By.id("user")));
        userSelect.selectByIndex(0);

        checkElementBy(driver, "free", "//button[@type='submit']").get(0).click();
    }

    public static void deleteFirstMarkInFilteredList(WebDriver driver) {
        checkElementBy(driver, "free", "//table[@id='marksTable']//a[contains(@href,'/mark/delete/')]").get(0).click();
    }

    public static boolean hasDeleteLink(WebDriver driver) {
        return !driver.findElements(By.xpath("//table[@id='marksTable']//a[contains(@href,'/mark/delete/')]")).isEmpty();
    }

    public static void assertTextPresent(WebDriver driver, String text) {
        SeleniumUtils.textIsPresentOnPage(driver, text);
    }

    public static void assertTextNotPresent(WebDriver driver, String text) {
        List<WebElement> list = driver.findElements(By.xpath("//*[contains(text(),'" + text + "')]"));
        Assertions.assertEquals(0, list.size());
    }

    public static boolean hasText(WebDriver driver, String text) {
        return !driver.findElements(By.xpath("//*[contains(text(),'" + text + "')]")).isEmpty();
    }
}
