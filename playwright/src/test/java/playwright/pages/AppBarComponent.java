package playwright.pages;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppBarComponent {

    private PageFactory pageFactory;

    public void navigateToDashboard() {
        pageFactory.getPage().locator("[data-ref=app-bar]").locator("[data-ref=dashboard]").click();
    }

    public void navigateToTransactions() {
        pageFactory.getPage().locator("[data-ref=app-bar]").locator("[data-ref=transactions]").click();
    }


}
