package playwright.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import playwright.model.BankAccount;

import static playwright.utils.AmountUtils.setAmount;


@Service
@AllArgsConstructor
public class BankAccountOverviewComponent {

    public static final String ADD_BANK_ACCOUNT = "[data-ref=add-bank-account]";
    public static final String BANK_ACCOUNT_DIALOG = "[data-ref=bank-account-dialog]";
    public static final String NAME = "[data-ref=name]";
    public static final String START_DATE = "[data-ref=start-date] >> input";
    public static final String START_BALANCE = "[data-ref=start-balance]";
    public static final String SAVE = "[data-ref=save]";

    private PageFactory pageFactory;
    private DatePickerComponent datePickerComponent;

    public void createBankAccount(BankAccount bankAccount) {
        openCreateBankAccountDialog();
        Page page = pageFactory.getPage();

        page.locator(BANK_ACCOUNT_DIALOG)
                .locator(NAME)
                .fill(bankAccount.getName());

        page.locator(BANK_ACCOUNT_DIALOG)
                .locator(START_DATE)
                .click();

        datePickerComponent.setDate(bankAccount.getStartDate());

        Locator locatorAmount = page.locator(BANK_ACCOUNT_DIALOG)
                .locator(START_BALANCE);

        setAmount(bankAccount.getStartBalance(), locatorAmount);

        page.locator(BANK_ACCOUNT_DIALOG)
                .locator(SAVE)
                .click();
    }

    private void openCreateBankAccountDialog() {
        pageFactory.getPage().locator(ADD_BANK_ACCOUNT).click();
    }

}
