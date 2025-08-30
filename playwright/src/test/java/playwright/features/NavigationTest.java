package playwright.features;

import org.junit.jupiter.api.Test;
import playwright.BaseSystemtest;
import playwright.pages.AppBarComponent;
import playwright.pages.TransactionOverviewComponent;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NavigationTest extends BaseSystemtest {

    @Test
    public void navigateToTransactionsTest() {
        AppBarComponent appBarComponent = new AppBarComponent(getPage());
        TransactionOverviewComponent transactionOverviewComponent = new TransactionOverviewComponent(getPage());

        assertFalse(transactionOverviewComponent.isVisible());
        appBarComponent.navigateToTransactions();

        transactionOverviewComponent.assertVisibility();
        assertTrue(transactionOverviewComponent.isVisible());

        appBarComponent.navigateToDashboard();
        assertFalse(transactionOverviewComponent.isVisible());
        
    }
}
