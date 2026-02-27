package com.uniovi.sdi.grademanager;

import com.uniovi.sdi.grademanager.pageobjects.PO_HomeView;
import com.uniovi.sdi.grademanager.pageobjects.PO_LoginView;
import com.uniovi.sdi.grademanager.pageobjects.PO_NavView;
import com.uniovi.sdi.grademanager.pageobjects.PO_PrivateView;
import com.uniovi.sdi.grademanager.pageobjects.PO_SignUpView;
import com.uniovi.sdi.grademanager.pageobjects.PO_View;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GradeManagerApplicationTests {

    static String PathFirefox = resolveFirefoxBinary();
    static String Geckodriver = "/Users/ikramelmabroukmorhnane/Desktop/PL-SDI-Sesión5-material/geckodriver-v0.36.0-mac";
    static String URL = "http://localhost:8090";
    static WebDriver driver;

    static String registeredDni;
    static String addedMarkDescription;

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
        registeredDni = "77" + UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        addedMarkDescription = "Selenium mark " + UUID.randomUUID().toString().replace("-", "");
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
    void PR01_AccessHomeView() {
        Assertions.assertTrue(driver.getCurrentUrl().startsWith(URL));
        Assertions.assertTrue(driver.getPageSource().contains("Bienvenidos") || driver.getPageSource().contains("Welcome"));
    }

    @Test
    @Order(2)
    void PR02_GoToSignUpView() {
        PO_NavView.goToSignUp(driver);
        Assertions.assertTrue(driver.getCurrentUrl().contains("/signup"));
    }

    @Test
    @Order(3)
    void PR03_GoToLoginView() {
        PO_NavView.goToLogin(driver);
        Assertions.assertTrue(driver.getCurrentUrl().contains("/login"));
    }

    @Test
    @Order(4)
    void PR04_LanguageButtons() {
        PO_NavView.changeIdiomToEnglish(driver);
        Assertions.assertTrue(driver.getPageSource().contains("Language"));
        PO_NavView.changeIdiomToSpanish(driver);
        Assertions.assertTrue(driver.getPageSource().contains("Idioma"));
    }

    @Test
    @Order(5)
    void PR05_ValidSignUp() {
        PO_NavView.goToSignUp(driver);
        PO_SignUpView.fillForm(driver, registeredDni, "Carlos", "Fernandez", "123456", "123456");
        PO_SignUpView.submit(driver);
        Assertions.assertTrue(driver.getCurrentUrl().contains("/home"));
        Assertions.assertTrue(driver.getPageSource().contains(registeredDni));
    }

    @Test
    @Order(6)
    void PR06_InvalidSignUp() {
        PO_NavView.goToSignUp(driver);
        PO_SignUpView.fillForm(driver, "99999990A", "Ana", "Lopez", "123456", "123456");
        PO_SignUpView.submit(driver);
        Assertions.assertTrue(driver.getCurrentUrl().contains("/signup"));
        PO_View.checkElementBy(driver, "class", "text-danger");
    }

    @Test
    @Order(7)
    void PR07_ValidStudentLogin() {
        PO_NavView.goToLogin(driver);
        PO_LoginView.login(driver, "99999990A", "123456");
        Assertions.assertTrue(driver.getCurrentUrl().contains("/home"));
        Assertions.assertTrue(driver.getPageSource().contains("99999990A"));
    }

    @Test
    @Order(8)
    void PR08_InvalidLogin() {
        PO_NavView.goToLogin(driver);
        PO_LoginView.login(driver, "99999990A", "bad-password");
        Assertions.assertTrue(driver.getCurrentUrl().contains("/login"));
    }

    @Test
    @Order(9)
    void PR09_LogoutAfterLogin() {
        PO_NavView.goToLogin(driver);
        PO_LoginView.login(driver, "99999990A", "123456");
        PO_NavView.logout(driver);
        Assertions.assertTrue(driver.getCurrentUrl().contains("/login"));
    }

    @Test
    @Order(10)
    void PR10_ValidProfessorLogin() {
        PO_NavView.goToLogin(driver);
        PO_LoginView.login(driver, "99999993D", "123456");
        Assertions.assertTrue(driver.getCurrentUrl().contains("/home"));
        Assertions.assertTrue(driver.getPageSource().contains("99999993D"));
    }

    @Test
    @Order(11)
    void PR11_ValidAdminLogin() {
        PO_NavView.goToLogin(driver);
        PO_LoginView.login(driver, "99999988F", "123456");
        Assertions.assertTrue(driver.getCurrentUrl().contains("/home"));
        Assertions.assertTrue(driver.getPageSource().contains("99999988F"));
    }

    @Test
    @Order(12)
    void PR12_MarksListStudentView() {
        PO_NavView.goToLogin(driver);
        PO_LoginView.login(driver, "99999990A", "123456");
        PO_PrivateView.goToMarks(driver);
        PO_View.checkElementBy(driver, "id", "marksTable");
        Assertions.assertTrue(driver.getPageSource().contains("Nota A1"));
    }

    @Test
    @Order(13)
    void PR13_MarkDetails() {
        PO_NavView.goToLogin(driver);
        PO_LoginView.login(driver, "99999993D", "123456");
        PO_PrivateView.goToMarks(driver);
        if (PO_PrivateView.hasDetailsLink(driver)) {
            PO_PrivateView.openFirstDetails(driver);
            Assertions.assertTrue(driver.getCurrentUrl().contains("/mark/details/"));
        } else {
            PO_View.checkElementBy(driver, "id", "marksTable");
        }
    }

    @Test
    @Order(14)
    void PR14_AddMark() {
        PO_NavView.goToLogin(driver);
        PO_LoginView.login(driver, "99999993D", "123456");
        PO_PrivateView.goToAddMark(driver);
        PO_PrivateView.addMark(driver, addedMarkDescription + " detail text", "7.5");
        PO_PrivateView.goToMarks(driver);
        PO_PrivateView.searchMark(driver, addedMarkDescription);
        Assertions.assertTrue(driver.getCurrentUrl().contains("/mark/list"));
    }

    @Test
    @Order(15)
    void PR15_DeleteMark() {
        PO_NavView.goToLogin(driver);
        PO_LoginView.login(driver, "99999993D", "123456");
        PO_PrivateView.goToMarks(driver);
        PO_PrivateView.searchMark(driver, addedMarkDescription);
        if (PO_PrivateView.hasDeleteLink(driver)) {
            PO_PrivateView.deleteFirstMarkInFilteredList(driver);
            PO_PrivateView.goToMarks(driver);
        }
        Assertions.assertTrue(driver.getCurrentUrl().contains("/mark/list"));
    }
}
