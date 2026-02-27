package com.uniovi.sdi.grademanager;

import com.uniovi.sdi.grademanager.pageobjects.PO_HomeView;
import com.uniovi.sdi.grademanager.pageobjects.PO_LoginView;
import com.uniovi.sdi.grademanager.pageobjects.PO_PrivateView;
import com.uniovi.sdi.grademanager.pageobjects.PO_Properties;
import com.uniovi.sdi.grademanager.pageobjects.PO_SignUpView;
import com.uniovi.sdi.grademanager.pageobjects.PO_View;
import com.uniovi.sdi.grademanager.util.SeleniumUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GradeManagerApplicationTests {

    static String PathFirefox = resolveFirefoxBinary();
    static String Geckodriver = "/Users/ikramelmabroukmorhnane/Desktop/PL-SDI-Sesión5-material/geckodriver-v0.36.0-mac";
    static String URL = "http://localhost:8090";
    static WebDriver driver;

    public static WebDriver getDriver(String pathFirefox, String geckodriver) {
        System.setProperty("webdriver.gecko.driver", geckodriver);
        FirefoxOptions options = new FirefoxOptions();
        options.setBinary(pathFirefox);
        return new FirefoxDriver(options);
    }

    private static String resolveFirefoxBinary() {
        String envBinary = System.getenv("FIREFOX_BIN");
        if (envBinary != null && Files.isExecutable(Path.of(envBinary))) {
            return envBinary;
        }
        String[] candidates = {
                "/Applications/Firefox.app/Contents/MacOS/firefox",
                "/Applications/Firefox.app/Contents/MacOS/firefox-bin",
                "/Applications/Firefox Developer Edition.app/Contents/MacOS/firefox",
                "/Applications/Firefox Developer Edition.app/Contents/MacOS/firefox-bin",
                System.getProperty("user.home") + "/Applications/Firefox.app/Contents/MacOS/firefox",
                System.getProperty("user.home") + "/Applications/Firefox.app/Contents/MacOS/firefox-bin"
        };
        for (String candidate : candidates) {
            if (Files.isExecutable(Path.of(candidate))) {
                return candidate;
            }
        }
        throw new IllegalStateException("Firefox binary not found. Set FIREFOX_BIN to the executable path.");
    }

    @BeforeAll
    static public void begin() {
        driver = getDriver(PathFirefox, Geckodriver);
    }

    @BeforeEach
    public void setUp() {
        driver.navigate().to(URL);
    }

    @AfterEach
    public void tearDown() {
        driver.manage().deleteAllCookies();
    }

    @AfterAll
    static public void end() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    void PR01A() {
        PO_HomeView.checkWelcomeToPage(driver, PO_Properties.getSPANISH());
    }

    @Test
    @Order(2)
    void PR01B() {
        List<WebElement> welcomeMessageElement = PO_HomeView.getWelcomeMessageText(driver, PO_Properties.getSPANISH());
        Assertions.assertEquals(welcomeMessageElement.getFirst().getText(), PO_HomeView.getP().getString("welcome.message", PO_Properties.getSPANISH()));
    }

    @Test
    @Order(3)
    void PR02() {
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
    }

    @Test
    @Order(4)
    void PR03() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
    }

    @Test
    @Order(5)
    void PR04() {
        PO_HomeView.checkChangeLanguage(driver, "btnSpanish", "btnEnglish", PO_Properties.getSPANISH(), PO_Properties.getENGLISH());
    }

    // PR05. Prueba del formulario de registro. registro con datos correctos
    @Test
    @Order(6)
    public void PR05() {
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        PO_SignUpView.fillForm(driver, "77777778A", "Josefo", "Perez", "77777", "77777");
        String checkText = "Notas del usuario";
        List<WebElement> result = PO_HomeView.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.getFirst().getText());
    }

    // PR06A. Prueba del formulario de registro. DNI repetido en la BD
    @Test
    @Order(7)
    public void PR06A() {
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        PO_SignUpView.fillForm(driver, "99999990A", "Josefo", "Perez", "77777", "77777");
        List<WebElement> result = PO_SignUpView.checkElementByKey(driver, "Error.signup.dni.duplicate", PO_Properties.getSPANISH());
        String checkText = PO_HomeView.getP().getString("Error.signup.dni.duplicate", PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.getFirst().getText());
    }

    // PR06B. Prueba del formulario de registro. Nombre corto.
    @Test
    @Order(8)
    public void PR06B() {
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        PO_SignUpView.fillForm(driver, "99999990B", "Jose", "Perez", "77777", "77777");
        List<WebElement> result = PO_SignUpView.checkElementByKey(driver, "Error.signup.name.length", PO_Properties.getSPANISH());
        String checkText = PO_HomeView.getP().getString("Error.signup.name.length", PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.getFirst().getText());
    }

    @Test
    @Order(9)
    public void PR07() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "99999990A", "123456");
        String checkText = "Notas del usuario";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.getFirst().getText());
    }

    @Test
    @Order(10)
    public void PR08() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "99999993D", "123456");
        String checkText = "Notas del usuario";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.getFirst().getText());
    }

    @Test
    @Order(11)
    public void PR09() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "99999988F", "123456");
        String checkText = "Notas del usuario";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.getFirst().getText());
    }

    @Test
    @Order(12)
    public void PR10() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "99999990A", "12345");
        PO_View.checkElementBy(driver, "text", "Identificate");
    }

    @Test
    @Order(13)
    public void PR11() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "99999990A", "123456");
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
    }

    @Test
    @Order(14)
    public void PR12() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "99999990A", "123456");
        String checkText = "Notas del usuario";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        List<WebElement> marksList = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr", PO_View.getTimeout());
        Assertions.assertEquals(4, marksList.size());
        String loginText = PO_HomeView.getP().getString("signup.message", PO_Properties.getSPANISH());
        PO_PrivateView.clickOption(driver, "logout", "text", loginText);
    }

    @Test
    @Order(15)
    public void PR13() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "99999990A", "123456");
        String checkText = "Notas del usuario";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        By enlace = By.xpath("//td[contains(text(), 'Nota A2')]/following-sibling::*[2]");
        driver.findElement(enlace).click();
        checkText = "Detalles de la nota";
        result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.getFirst().getText());
        String loginText = PO_HomeView.getP().getString("signup.message", PO_Properties.getSPANISH());
        PO_PrivateView.clickOption(driver, "logout", "text", loginText);
    }

    @Test
    @Order(16)
    public void PR14() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "99999993D", "123456");
        PO_View.checkElementBy(driver, "text", "99999993D");
        List<WebElement> elements = PO_View.checkElementBy(driver, "free", "//*[@id='myNavbar']/ul[1]/li[2]");
        elements.getFirst().click();
        elements = PO_View.checkElementBy(driver, "free", "//a[contains(@href, 'mark/add')]");
        elements.getFirst().click();
        String checkText = "Nota sistemas distribuidos";
        PO_PrivateView.fillFormAddMark(driver, 3, checkText, "8");
        elements = PO_View.checkElementBy(driver, "free", "//a[contains(@class, 'page-link')]");
        elements.getLast().click();
        elements = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, elements.getFirst().getText());
        String loginText = PO_HomeView.getP().getString("signup.message", PO_Properties.getSPANISH());
        PO_PrivateView.clickOption(driver, "logout", "text", loginText);
    }

    @Test
    @Order(17)
    public void PR15() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "99999993D", "123456");
        PO_View.checkElementBy(driver, "text", "99999993D");
        List<WebElement> elements = PO_View.checkElementBy(driver, "free", "//*[@id='myNavbar']/ul[1]/li[2]");
        elements.getFirst().click();
        elements = PO_View.checkElementBy(driver, "free", "//a[contains(@href, 'mark/list')]");
        elements.getFirst().click();
        elements = PO_View.checkElementBy(driver, "free", "//a[contains(@class, 'page-link')]");
        elements.getLast().click();
        elements = PO_View.checkElementBy(driver, "free", "//td[contains(text(), 'Nota sistemas distribuidos')]/following-sibling::*/a[contains(@href, 'mark/delete')]");
        elements.getFirst().click();
        elements = PO_View.checkElementBy(driver, "free", "//a[contains(@class, 'page-link')]");
        elements.getLast().click();
        SeleniumUtils.waitTextIsNotPresentOnPage(driver, "Nota sistemas distribuidos", PO_View.getTimeout());
        String loginText = PO_HomeView.getP().getString("signup.message", PO_Properties.getSPANISH());
        PO_PrivateView.clickOption(driver, "logout", "text", loginText);
    }
}
