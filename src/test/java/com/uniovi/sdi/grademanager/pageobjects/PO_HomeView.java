package com.uniovi.sdi.grademanager.pageobjects;

import org.openqa.selenium.WebDriver;

public class PO_HomeView extends PO_NavView {


    //Se hizo en el commit anteriro sin querer.
    public static void checkWelcomeMessage(WebDriver driver, int locale) {
        checkElementByKey(driver, "welcome.message", locale);
    }
}
