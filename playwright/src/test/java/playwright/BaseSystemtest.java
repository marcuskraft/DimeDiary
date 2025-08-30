package playwright;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseSystemtest {

    private static Playwright playwright;
    private static Browser browser;
    private static Page page;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
        Response response = page.navigate("http://localhost:4200/dashboard");

        assertTrue(response.ok());
    }

    @AfterAll
    static void closeBrowser() {
        playwright.close();
    }

    public static Page getPage() {
        return page;
    }
}
