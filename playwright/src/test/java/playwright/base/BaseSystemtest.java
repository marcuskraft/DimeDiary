package playwright.base;

import com.microsoft.playwright.*;
import lombok.Getter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import playwright.TestApplication;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ContextConfiguration(classes = TestApplication.class)
public class BaseSystemtest {

    private static Playwright playwright;
    @Getter
    private static Page page;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
        Response response = page.navigate("http://localhost:4200/dashboard");

        assertTrue(response.ok());
    }

    @AfterAll
    static void closeBrowser() {
        playwright.close();
    }


}
