package org.hw4.selenide;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;

public class LoginPageSelenide {

    @FindBy(css="form#login input[type='text']")
    private SelenideElement usernameField;
    @FindBy(css="form#login input[type='password']")
    private SelenideElement passwordField;
    @FindBy(css="form#login button")
    private SelenideElement loginButton;
    @FindBy(css = "div.error-block")
    private SelenideElement errorBlock;



    public void login(String username, String password) {
        typeUsernameInField(username);
        typePasswordInField(password);
        clickLoginButton();
    }

    public void typeUsernameInField(String username) {
        usernameField.shouldBe(Condition.visible).sendKeys(username);
    }

    public void typePasswordInField(String password) {
       passwordField.shouldBe(Condition.visible).sendKeys(password);
    }

    public void clickLoginButton() {
        loginButton.shouldBe(Condition.visible).click();
    }

    public String getErrorBlockText() {
        return errorBlock.shouldBe(Condition.visible)
                .getText().replace("\n", " ");
    }

}
