package com.uniovi.sdi.grademanager.pageobjects;

import org.openqa.selenium.WebDriver;

public class PO_NavView extends PO_View {

    public static void goToSignUp(WebDriver driver) {
        checkElementBy(driver, "free", "//a[@href='/signup']").get(0).click();
    }

    public static void goToLogin(WebDriver driver) {
        checkElementBy(driver, "free", "//a[@href='/login']").get(0).click();
    }

    public static void logout(WebDriver driver) {
        checkElementBy(driver, "free", "//a[@href='/logout']").get(0).click();
    }

    public static void changeIdiomToSpanish(WebDriver driver) {
        checkElementBy(driver, "id", "btnLanguage").get(0).click();
        checkElementBy(driver, "id", "btnSpanish").get(0).click();
    }

    public static void changeIdiomToEnglish(WebDriver driver) {
        checkElementBy(driver, "id", "btnLanguage").get(0).click();
        checkElementBy(driver, "id", "btnEnglish").get(0).click();
    }
}
