package org.hw4.selenide;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.hw4.GroupTableRow;
import org.hw4.StudentTableRow;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;

public class MainPageSelenide {
    private WebDriverWait wait;

    @FindBy(css = "nav li.mdc-menu-surface--anchor a")
    private SelenideElement usernameLinkInNavBar;
    @FindBy(xpath = "//ul[@role='menu']/li/span[text()='Profile']")
    private SelenideElement profileButton;

    // Create Group Modal Window
    @FindBy(id = "create-btn")
    private SelenideElement createGroupButton;
    @FindBy(xpath = "//form//span[contains(text(), 'Group name')]/following-sibling::input")
    private SelenideElement groupNameField;
    @FindBy(css = "form div.submit button")
    private SelenideElement submitButtonOnModalWindow;
    @FindBy(xpath = "//span[text()='Creating Study Group']" +
            "//ancestor::div[contains(@class, 'form-modal-header')]//button")
    private SelenideElement closeCreateGroupIcon;

    // Create Students Modal Window
    @FindBy(css = "div#generateStudentsForm-content input")
    private SelenideElement createStudentsFormInput;
    @FindBy(css = "div#generateStudentsForm-content div.submit button")
    private SelenideElement saveCreateStudentsForm;
    @FindBy(xpath = "//h2[@id='generateStudentsForm-title']/../button")
    private SelenideElement closeCreateStudentsFormIcon;

    @FindBy(xpath = "//table[@aria-label='Tutors list']/tbody/tr")
    private ElementsCollection rowsInGroupTable;
    @FindBy(xpath = "//table[@aria-label='User list']/tbody/tr")
    private ElementsCollection rowsInStudentTable;

    public SelenideElement waitAndGetGroupTitleByText(String title) {
        String xpath = String.format("//table[@aria-label='Tutors list']/tbody//td[text()='%s']", title);
        return $(By.xpath(xpath)).shouldBe(Condition.visible);
    }

    public void openProfilePage() {
        usernameLinkInNavBar.shouldBe(Condition.visible).click();
        profileButton.shouldBe(Condition.visible).click();
    }

    public void createGroup(String groupName) {
        wait.until(ExpectedConditions.visibilityOf(createGroupButton)).click();
        wait.until(ExpectedConditions.visibilityOf(groupNameField)).sendKeys(groupName);
        submitButtonOnModalWindow.click();
        waitAndGetGroupTitleByText(groupName);
    }

    public void closeCreateGroupModalWindow() {
        closeCreateGroupIcon.click();
        wait.until(ExpectedConditions.invisibilityOf(closeCreateGroupIcon));
    }

    public void typeAmountOfStudentsInCreateStudentsForm(int amount) {
        wait.until(ExpectedConditions.visibilityOf(createStudentsFormInput))
                .sendKeys(String.valueOf(amount));
    }

    public void clickSaveButtonOnCreateStudentsForm() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOf(saveCreateStudentsForm)).click();
        // Использование подобного ожидания - плохая практика, желательно стараться избегать
        // Но в некоторых очень редких случаях, при условии что вы, например, обсудили это
        // с участниками команды, можно оставить и так. Это может зависеть от соотношения
        // потраченных усилий на реализацию без sleep и частоты вызовы этого метода
        Thread.sleep(5000);
    }

    public void closeCreateStudentsModalWindow() {
        closeCreateStudentsFormIcon.click();
        wait.until(ExpectedConditions.invisibilityOf(closeCreateStudentsFormIcon));
    }

    public String getUsernameLabelText() {
        return usernameLinkInNavBar.shouldBe(Condition.visible)
                .getText().replace("\n", " ");
    }

    // Group Table Section
    public void clickTrashIconOnGroupWithTitle(String title) {
        getGroupRowByTitle(title).clickTrashIcon();
    }

    public void clickRestoreFromTrashIconOnGroupWithTitle(String title) {
        getGroupRowByTitle(title).clickRestoreFromTrashIcon();
    }

    public void clickAddStudentsIconOnGroupWithTitle(String title) {
        getGroupRowByTitle(title).clickAddStudentsIcon();
    }

    public void clickZoomInIconOnGroupWithTitle(String title) {
        getGroupRowByTitle(title).clickZoomInIcon();
    }

    public String getStatusOfGroupWithTitle(String title) {
        return getGroupRowByTitle(title).getStatus();
    }

    private GroupTableRow getGroupRowByTitle(String title) {
        return rowsInGroupTable.stream()
                .map(GroupTableRow::new)
                .filter(row -> row.getTitle().equals(title))
                .findFirst().orElseThrow();
    }

    // Students Table Section

    public void clickTrashIconOnStudentWithName(String name) {
        getStudentRowByName(name).clickTrashIcon();
    }

    public void clickRestoreFromTrashIconOnStudentWithName(String name) {
        getStudentRowByName(name).clickRestoreFromTrashIcon();
    }

    public String getStatusOfStudentWithName(String name) {
        return getStudentRowByName(name).getStatus();
    }

    public String getFirstGeneratedStudentName() {
        rowsInStudentTable.forEach(element -> element.shouldBe(Condition.visible));
        return rowsInStudentTable.stream()
                .map(StudentTableRow::new)
                .findFirst().orElseThrow().getName();
    }

    private StudentTableRow getStudentRowByName(String name) {
        rowsInStudentTable.forEach(element -> element.shouldBe(Condition.visible));
        return rowsInStudentTable.stream()
                .map(StudentTableRow::new)
                .filter(row -> row.getName().equals(name))
                .findFirst().orElseThrow();
    }
}
