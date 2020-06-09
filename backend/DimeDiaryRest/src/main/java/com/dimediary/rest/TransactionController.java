package com.dimediary.rest;

import com.dimediary.openapi.api.TransactionApi;
import com.dimediary.openapi.model.Transaction;
import com.dimediary.openapi.model.Transactions;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api")
@Controller
public class TransactionController implements TransactionApi {

  @Override
  public ResponseEntity<Transactions> transactionGet() {

    final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    final Transactions transactions = new Transactions();

    for (int i = 1; i <= 31; i++) {
      final LocalDate date = LocalDate.of(2020, 1, i);
      final Transaction transaction = new Transaction();
      transaction.setAmount(Integer.valueOf(i));
      transaction.setDate(dateTimeFormatter.format(date));
      transaction.setSubject("test Transaction " + i);

      transactions.addTransactionsItem(transaction);
    }

    return ResponseEntity.ok().body(transactions);
  }

}
