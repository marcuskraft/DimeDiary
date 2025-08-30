package playwright.pages;

import com.microsoft.playwright.Locator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class TransactionComponent {

    public static final String TRANSACTION_PAGE = "[data-ref=transaction-page]";
    public static final String NAME = "[data-ref=name]";
    public static final String AMOUNT = "[data-ref=amount]";
    public static final String DATE = "[data-ref=date] >> input";

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
        String amountString = String.format("%.2f", amount);
        Locator locator = pageFactory.getPage().locator(TRANSACTION_PAGE)
                .locator(AMOUNT);
        locator.fill("");
        locator.pressSequentially(amountString, new Locator.PressSequentiallyOptions().setDelay(50));
    }

    public void save() {
        pageFactory.getPage().locator(TRANSACTION_PAGE)
                .locator("[data-ref=save]")
                .click();
    }
}

