package playwright.pages;

import com.microsoft.playwright.Locator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import playwright.utils.AmountUtils;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class TransactionComponent {

    private static final String TRANSACTION_PAGE = "[data-ref=transaction-page]";
    private static final String NAME = "[data-ref=name]";
    private static final String AMOUNT = "[data-ref=amount]";
    private static final String DATE = "[data-ref=date] >> input";
    private static final String SAVE = "[data-ref=save]";

    private PageFactory pageFactory;
    private DatePickerComponent datePickerComponent;


    public void setName(String name) {
        pageFactory.getPage().locator(TRANSACTION_PAGE)
                .locator(NAME)
                .fill(name);
    }

    public void setDATE(LocalDate localDate) {
        pageFactory.getPage().locator(TRANSACTION_PAGE).locator(DATE).click();
        datePickerComponent.setDate(localDate);
    }

    public void setAmount(Double amount) {
        Locator locator = pageFactory.getPage()
                .locator(TRANSACTION_PAGE)
                .locator(AMOUNT);
        AmountUtils.setAmount(amount, locator);
    }


    public void save() {
        pageFactory.getPage().locator(TRANSACTION_PAGE)
                .locator(SAVE)
                .click();
    }
}

