package playwright.pages;

import com.microsoft.playwright.Page;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AppBarComponent {

    private Page page;

    public void navigateToDashboard() {
        page.locator("[data-ref=app-bar]").locator("[data-ref=dashboard]").click();
    }

    public void navigateToTransactions() {
        page.locator("[data-ref=app-bar]").locator("[data-ref=transactions]").click();
    }


}
