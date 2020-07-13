package com.dimediary.rest.controller;

import com.dimediary.domain.BankAccount;
import com.dimediary.openapi.api.TransactionApi;
import com.dimediary.openapi.model.Transactions;
import com.dimediary.port.in.BankAccountProvider;
import com.dimediary.port.in.TransactionProvider;
import com.dimediary.rest.converter.TransactionConverter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping("/api")
@Controller
public class TransactionController implements TransactionApi {

  public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
      .ofPattern("dd.MM.yyyy");


  private final BankAccountProvider bankAccountProvider;

  private final TransactionProvider transactionProvider;

  private final TransactionConverter transactionConverter;

  @Autowired
  public TransactionController(final BankAccountProvider bankAccountProvider,
      final TransactionProvider transactionProvider,
      final TransactionConverter transactionConverter) {
    this.bankAccountProvider = bankAccountProvider;
    this.transactionProvider = transactionProvider;
    this.transactionConverter = transactionConverter;
  }

  @Override
  public ResponseEntity<Transactions> transactionGet(
      final String dateFrom, final String dateUntil) {

    final LocalDate localDateFrom = LocalDate.parse(dateFrom, DATE_TIME_FORMATTER);
    final LocalDate localDateUntil = LocalDate.parse(dateUntil, DATE_TIME_FORMATTER);

    final List<String> bankAccounts = this.bankAccountProvider.getBankAccountNames();

    if (bankAccounts.size() == 0) {
      return ResponseEntity.noContent().build();
    }

    final BankAccount bankAccount = this.bankAccountProvider.getBankAccount(bankAccounts.get(0));

    final List<com.dimediary.domain.Transaction> transactionsDomain = this.transactionProvider
        .getTransactions(localDateFrom, localDateUntil, bankAccount);

    final Transactions transactions = new Transactions();
    for (final com.dimediary.domain.Transaction transaction : transactionsDomain) {
      transactions.addTransactionsItem(this.transactionConverter.from(transaction));
    }

    this.log.info("test");
    return ResponseEntity.ok().body(transactions);

  }
}
