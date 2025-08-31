package playwright.features;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import playwright.base.BaseSystemtest;
import playwright.model.Transaction;
import playwright.pages.AppBarComponent;
import playwright.pages.TransactionComponent;
import playwright.pages.TransactionOverviewComponent;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Component
public class TransactionTest extends BaseSystemtest {

    @Autowired
    private AppBarComponent appBarComponent;
    @Autowired
    private TransactionOverviewComponent transactionOverviewComponent;
    @Autowired
    private TransactionComponent transactionComponent;

    @Test
    public void createTransaction() {
        appBarComponent.navigateToTransactions();
        transactionOverviewComponent.assertVisibility();

        transactionOverviewComponent.openCreateNewTransactionPage();

        String name = "My fancy transaction";
        double amount = 123.43;
        LocalDate localDate = LocalDate.now().plusDays(4);

        transactionComponent.setName(name);
        transactionComponent.setDATE(localDate);
        transactionComponent.setAmount(amount);
        transactionComponent.save();

        List<Transaction> allTransactions = transactionOverviewComponent.getAllTransactions();

        Optional<Transaction> myFancyTransaction = allTransactions.stream()
                .filter(transaction -> transaction.getName().equals(name))
                .findFirst();

        assertThat(myFancyTransaction).isPresent();
        Transaction transaction = myFancyTransaction.get();
        assertThat(transaction.getAmount()).isEqualTo(amount);
        assertThat(transaction.getLocalDate()).isEqualTo(localDate);
    }
}
