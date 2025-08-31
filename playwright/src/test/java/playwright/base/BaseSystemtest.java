package playwright.base;

import com.microsoft.playwright.*;
import lombok.Getter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import playwright.TestApplication;
import playwright.model.BankAccount;
import playwright.pages.AppBarComponent;
import playwright.pages.BankAccountOverviewComponent;
import playwright.pages.DatePickerComponent;
import playwright.pages.PageFactory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ContextConfiguration(classes = TestApplication.class)
public class BaseSystemtest {

    private static Playwright playwright;
    @Getter
    private static Page page;
    private static Browser browser;
    private static boolean initialized = false;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
        Response response = page.navigate("http://localhost:4200/dashboard");

        assertTrue(response.ok());

        if (!initialized) {
            initialized = true;
            initBankAccount();
        }
    }

    @AfterAll
    static void closeBrowser() {
        browser.close();
        playwright.close();
    }

    static void initBankAccount() {
        PageFactory pageFactory = new PageFactory();
        BankAccountOverviewComponent bankAccountOverviewComponent = new BankAccountOverviewComponent(pageFactory, new DatePickerComponent(pageFactory));
        AppBarComponent appBarComponent = new AppBarComponent(pageFactory);

        BankAccount bankAccount = BankAccount.builder()
                .name("TestAccount")
                .startBalance(100.0)
                .startDate(LocalDate.now().minusMonths(1))
                .build();

        appBarComponent.navigateToBankAccounts();
        bankAccountOverviewComponent.createBankAccount(bankAccount);
    }


}
