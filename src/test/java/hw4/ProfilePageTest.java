package hw4;

import com.codeborne.selenide.Configuration;
import static com.codeborne.selenide.Selenide.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.cdimascio.dotenv.Dotenv;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.hw4.ProfilePage;
import org.hw4.selenide.LoginPageSelenide;
import org.hw4.selenide.MainPageSelenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic(value = "Тестирование Страницы профиля")
@Feature(value = "Промежуточная аттестация")
public class ProfilePageTest {
    private static String USERNAME;
    private static String PASSWORD;
    private static LocalDate DATE;
    static LoginPageSelenide loginPage;
    static MainPageSelenide mainPage;
    static ProfilePage profilePage;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @BeforeAll
    public static void setupClass() {
        Configuration.remote = "http://localhost:4444/wd/hub";
        Configuration.browser = "chrome";
       // WebDriverManager.chromedriver().setup();
        Dotenv dotenv = Dotenv.load();
        DATE = LocalDate.of(1997, 6,5);
        USERNAME = dotenv.get("geekbrains_username");
        PASSWORD = dotenv.get("geekbrains_password");
    }

    @BeforeEach
    void init(){
        //Configuration.fastSetValue = true;
        loginPage = page(LoginPageSelenide.class);
        open("https://test-stand.gb.ru/login");
    }

    @Test
    @Description("Тест невалидных данных для входа")
    public void testLoginWithEmptyFields() {
        loginPage.clickLoginButton();
        assertEquals("401 Invalid credentials.", loginPage.getErrorBlockText());
    }

    @Test
    @Description("Тест изменение значения в поле Date of birth")
    void testChangeBirthday(){
        loginPage.login(USERNAME, PASSWORD);
        mainPage = page(MainPageSelenide.class);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
        mainPage.openProfilePage();
        profilePage = page(ProfilePage.class);
        assertTrue(profilePage.getTitlePageText().contains("Profile"));
        profilePage.openOptionsWindow();
        sleep(1000l);
        profilePage.setDateOfBirthday(DATE);
        profilePage.setPhoneNumber("79812346588");
        profilePage.clickSaveButtonOnEditForm();
        profilePage.clickCloseButtonForm();
        assertTrue(profilePage.getDateOfBirthText().contains(DATE.format(formatter)));
    }
}
