package hw4;

import io.github.cdimascio.dotenv.Dotenv;
import org.hw4.LoginPage;
import org.hw4.MainPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.remote.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Пример использования самых базовых методов библиотеки Selenium.
 */
public class GeekBrainsStandTests {

    private RemoteWebDriver driver;
    //private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;
    private MainPage mainPage;
    private static String USERNAME;
    private static String PASSWORD;

    @BeforeAll
    public static void setupClass() {
        Dotenv dotenv = Dotenv.load();
        //WebDriverManager.chromedriver().setup();
        // Помещаем в переменные окружения путь до драйвера
        //System.setProperty("webdriver.chrome.driver", "src\\test\\resources\\chromedriver.exe");
        // mvn clean test -Dgeekbrains_username=USER -Dgeekbrains_password=PASS
        USERNAME = dotenv.get("geekbrains_username");
        PASSWORD = dotenv.get("geekbrains_password");
        //USERNAME = System.getProperty("geekbrains_username", System.getenv("geekbrains_username"));
        //PASSWORD = System.getProperty("geekbrains_password", System.getenv("geekbrains_password"));
    }
    @BeforeEach
    public void setupTest() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        options.setCapability("browserVersion", "124.0");
        options.setCapability("selenoid:options", new HashMap<String, Object>() {{
            /* How to add test badge */
            put("name", "Test badge...");

            /* How to set session timeout */
            put("sessionTimeout", "15m");

            /* How to set timezone */
            put("env", new ArrayList<String>() {{
                add("TZ=UTC");
            }});

            /* How to add "trash" button */
            put("labels", new HashMap<String, Object>() {{
                put("manual", "true");
            }});

            /* How to enable video recording */
            put("enableVideo", true);
        }});
        //options.addArguments("--start-maxized");
        // Создаём экземпляр драйвера
        driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options);
        //driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        // Растягиваем окно браузера на весь экран
        //driver.manage().window().maximize();
        // Навигация на https://test-stand.gb.ru/login
        driver.get("https://test-stand.gb.ru/login");
        // Объект созданного Page Object
        loginPage = new LoginPage(driver, wait);
    }

    @Test
    public void testLoginWithEmptyFields() {
        // Клик на кнопку LOGIN без ввода данных в поля
        loginPage.clickLoginButton();
        // Проверка, что появился блок с ожидаемой ошибкой
        assertEquals("401 Invalid credentials.", loginPage.getErrorBlockText());
    }

    @Test
    public void testAddingGroupOnMainPage() {
        // Логин в систему с помощью метода из класса Page Object
        loginPage.login(USERNAME, PASSWORD);
        // Инициализация объекта класса MainPage
        mainPage = new MainPage(driver, wait);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
        // Создание группы. Даём ей уникальное имя, чтобы в каждом запуске была проверка нового имени
        String groupTestName = "New Test Group " + System.currentTimeMillis();
        mainPage.createGroup(groupTestName);
        // Проверка, что группа создана и находится в таблице
        assertTrue(mainPage.waitAndGetGroupTitleByText(groupTestName).isDisplayed());
    }

    @Test
    void testArchiveGroupOnMainPage() {
        // Обычный логин + создание группы
        loginPage.login(USERNAME, PASSWORD);
        mainPage = new MainPage(driver, wait);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
        String groupTestName = "New Test Group " + System.currentTimeMillis();
        mainPage.createGroup(groupTestName);
        // Требуется закрыть модальное окно
        mainPage.closeCreateGroupModalWindow();
        // Изменение созданной группы с проверками
        assertEquals("active", mainPage.getStatusOfGroupWithTitle(groupTestName));
        mainPage.clickTrashIconOnGroupWithTitle(groupTestName);
        assertEquals("inactive", mainPage.getStatusOfGroupWithTitle(groupTestName));
        mainPage.clickRestoreFromTrashIconOnGroupWithTitle(groupTestName);
        assertEquals("active", mainPage.getStatusOfGroupWithTitle(groupTestName));
    }

    @Test
    void testBlockingStudentInTableOnMainPage() throws InterruptedException {
        // Обычный логин + создание группы
        loginPage.login(USERNAME, PASSWORD);
        mainPage = new MainPage(driver, wait);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
        String groupTestName = "New Test Group " + System.currentTimeMillis();
        mainPage.createGroup(groupTestName);
        // Требуется закрыть модальное окно
        mainPage.closeCreateGroupModalWindow();
        // Добавление студентов
        mainPage.clickAddStudentsIconOnGroupWithTitle(groupTestName);
        mainPage.typeAmountOfStudentsInCreateStudentsForm(3);
        mainPage.clickSaveButtonOnCreateStudentsForm();
        mainPage.closeCreateStudentsModalWindow();
        mainPage.clickZoomInIconOnGroupWithTitle(groupTestName);
        // Проверка переходов статуса первого студента из таблицы
        String firstGeneratedStudentName = mainPage.getFirstGeneratedStudentName();
        assertEquals("active", mainPage.getStatusOfStudentWithName(firstGeneratedStudentName));
        mainPage.clickTrashIconOnStudentWithName(firstGeneratedStudentName);
        assertEquals("block", mainPage.getStatusOfStudentWithName(firstGeneratedStudentName));
        mainPage.clickRestoreFromTrashIconOnStudentWithName(firstGeneratedStudentName);
        assertEquals("active", mainPage.getStatusOfStudentWithName(firstGeneratedStudentName));
    }

    @AfterEach
    public void teardown() {
        // Закрываем все окна брайзера и процесс драйвера
        driver.quit();
    }

}

