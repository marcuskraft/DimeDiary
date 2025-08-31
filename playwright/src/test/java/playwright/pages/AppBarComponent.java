package playwright.pages;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppBarComponent {

    private static final String APP_BAR = "[data-ref=app-bar]";
    private static final String DASHBOARD = "[data-ref=dashboard]";
    private static final String TRANSACTIONS = "[data-ref=transactions]";
    private static final String BANKACCOUNTS = "[data-ref=bankaccounts]";
    private PageFactory pageFactory;

    public void navigateToDashboard() {
        pageFactory.getPage().locator(APP_BAR).locator(DASHBOARD).click();
    }

    public void navigateToTransactions() {
        pageFactory.getPage().locator(APP_BAR).locator(TRANSACTIONS).click();
    }

    public void navigateToBankAccounts() {
        pageFactory.getPage().locator(APP_BAR).locator(BANKACCOUNTS).click();
    }


}
