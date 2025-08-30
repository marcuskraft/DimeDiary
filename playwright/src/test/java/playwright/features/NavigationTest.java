package playwright.features;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import playwright.base.BaseSystemtest;
import playwright.pages.AppBarComponent;
import playwright.pages.TransactionOverviewComponent;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Component
public class NavigationTest extends BaseSystemtest {

    @Autowired
    private AppBarComponent appBarComponent;
    @Autowired
    private TransactionOverviewComponent transactionOverviewComponent;

    @Test
    public void navigateToTransactionsTest() {
        assertFalse(transactionOverviewComponent.isVisible());
        appBarComponent.navigateToTransactions();

        transactionOverviewComponent.assertVisibility();
        assertTrue(transactionOverviewComponent.isVisible());

        appBarComponent.navigateToDashboard();
        assertFalse(transactionOverviewComponent.isVisible());

    }
}
