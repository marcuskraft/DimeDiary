package playwright.pages;

import com.microsoft.playwright.Locator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import playwright.model.Transaction;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionOverviewComponent {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final String TRANSACTION_GROUP = "[data-ref=transaction-group]";
    private static final String DATE = "[data-ref=date]";
    private static final String NAME = "[data-ref=name]";
    private static final String CATEGORY = "[data-ref=category]";
    private static final String AMOUNT = "[data-ref=amount]";
    private static final String BALANCE = "[data-ref=balance]";
    private static final String TRANSACTIONS_OVERVIEW = "[data-ref=transactions-overview]";
    private static final String ADD_TRANSACTION = "[data-ref=add-transaction]";

    private PageFactory pageFactory;


    public boolean isVisible() {
        return pageFactory.getPage().isVisible(TRANSACTIONS_OVERVIEW);
    }

    public void assertVisibility() {
        pageFactory.getPage().locator(TRANSACTIONS_OVERVIEW).waitFor();
    }

    public List<Transaction> getAllTransactions() {
        Locator transactionLocator = pageFactory.getPage().locator(TRANSACTION_GROUP);
        transactionLocator.first().waitFor();
        transactionLocator.first().locator(BALANCE).waitFor();
        return transactionLocator.all().stream()
                .map(locator -> {
                    String dateString = locator.locator(DATE).innerText();
                    LocalDate date = LocalDate.parse(dateString, DATE_TIME_FORMATTER);

                    String name = locator.locator(NAME).innerText();

                    String category = locator.locator(CATEGORY).innerText();

                    String amountString = locator.locator(AMOUNT).innerText();
                    amountString = normalizeAmountString(amountString);
                    Double amount = Double.parseDouble(amountString);

                    String balanceString = locator.locator(BALANCE).innerText();
                    balanceString = normalizeAmountString(balanceString);
                    Double balance = Double.parseDouble(balanceString);

                    return Transaction.builder()
                            .localDate(date)
                            .name(name)
                            .category(category)
                            .amount(amount)
                            .balance(balance)
                            .build();
                }).collect(Collectors.toList());
    }

    public void openCreateNewTransactionPage() {
        pageFactory.getPage().locator(TRANSACTIONS_OVERVIEW)
                .locator(ADD_TRANSACTION)
                .click();
    }

    private static String normalizeAmountString(String amount) {
        return amount
                .replace(" ", "")
                .replace("€", "")
                .replace(".", "")
                .replace(",", ".");
    }
}
