package org.hw4;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.SetValueOptions;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;
import java.time.LocalDate;

import static com.codeborne.selenide.Condition.*;

public class ProfilePage {

    @FindBy (css = "div h1.svelte-vyyzan")
    private SelenideElement titlePage;
    @FindBy (xpath = "//button[@title='More options']")
    private SelenideElement optionsButton;
    @FindBy (xpath = "//form[@id='update-item']//input[@type='date']")
    private SelenideElement inputDateOfBirthday;
    @FindBy (xpath = "//form[@id='update-item']//span[text()='Phone']/following-sibling::input")
    private SelenideElement inputPhoneNumber;
    @FindBy (xpath = "//form[@id='update-item']//button[@type='submit']")
    private SelenideElement saveButton;
    @FindBy (xpath = "//h2[@id='profileForm-title']/..//button[@data-mdc-dialog-action='close']")
    private SelenideElement closeButton;
    @FindBy (xpath = "//div[text()='Date of birth']//following-sibling::div")
    private SelenideElement dateOfBirth;

    public String getTitlePageText() {
        return titlePage.shouldBe(visible).text();
    }

    public void openOptionsWindow(){
        optionsButton.shouldBe(visible).click();
    }

    public void setDateOfBirthday(LocalDate date) {
        inputDateOfBirthday.shouldBe(visible).setValue(SetValueOptions.withDate(date));

    }

    public void clickSaveButtonOnEditForm() {
        saveButton.shouldBe(visible).click();
    }

    public void clickCloseButtonForm() {
        closeButton.shouldBe(visible).click();
    }

    public void setPhoneNumber(String number) {
        inputPhoneNumber.shouldBe(visible).setValue(number);
    }

    public String getDateOfBirthText(){
        return dateOfBirth.shouldBe(visible).getText();
    }
}
