package com.uniovi.sdi.grademanager.pageobjects;

import com.uniovi.sdi.grademanager.util.SeleniumUtils;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PO_NavView extends PO_View {

    /**
     * Clic en una de las opciones principales (a href) y comprueba que se vaya a la vista con el elemento de
     * tipo type con el texto Destino
     *
     * @param driver: apuntando al navegador abierto actualmente.
     * @param textOption: Texto de la opción principal.
     * @param criterio: "id" or "class" or "text" or "@attribute" or "free". Si el valor de criterio es free es una
     * expresion xpath completa.
     * @param targetText: texto correspondiente a la búsqueda de la página destino.
     */
    public static void clickOption(WebDriver driver, String textOption, String criterio, String targetText) {
        List<WebElement> elements = SeleniumUtils.waitLoadElementsBy(driver, "@href", textOption, getTimeout());
        Assertions.assertEquals(1, elements.size());
        elements.getFirst().click();
        elements = SeleniumUtils.waitLoadElementsBy(driver, criterio, targetText, getTimeout());
        Assertions.assertEquals(1, elements.size());
    }

    /**
     * Selecciona el enlace de idioma correspondiente al texto textLanguage
     *
     * @param driver: apuntando al navegador abierto actualmente.
     * @param textLanguage: el texto que aparece en el enlace de idioma ("English" o "Spanish")
     */
    public static void changeLanguage(WebDriver driver, String textLanguage) {
        List<WebElement> languageButton = SeleniumUtils.waitLoadElementsBy(driver, "id", "btnLanguage", getTimeout());
        languageButton.getFirst().click();
        SeleniumUtils.waitLoadElementsBy(driver, "id", "languageDropdownMenuButton", getTimeout());
        List<WebElement> selectedLanguage = SeleniumUtils.waitLoadElementsBy(driver, "id", textLanguage, getTimeout());
        selectedLanguage.getFirst().click();
    }
}
