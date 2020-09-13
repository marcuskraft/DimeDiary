package com.dimediary.rest.controller;

import com.dimediary.openapi.api.TransactionApi;
import com.dimediary.openapi.model.Transaction;
import com.dimediary.port.in.BankAccountProvider;
import com.dimediary.port.in.TransactionProvider;
import com.dimediary.rest.converter.TransactionRestConverter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

  private final TransactionRestConverter transactionRestConverter;

  @Autowired
  public TransactionController(final BankAccountProvider bankAccountProvider,
      final TransactionProvider transactionProvider,
      final TransactionRestConverter transactionRestConverter) {
    this.bankAccountProvider = bankAccountProvider;
    this.transactionProvider = transactionProvider;
    this.transactionRestConverter = transactionRestConverter;
  }


  @Override
  public ResponseEntity<Transaction> createTransaction(final Transaction transaction) {
    return null;
  }

  @Override
  public ResponseEntity<Void> deleteTransaction(final UUID transactionId) {
    return null;
  }

  @Override
  public ResponseEntity<List<Transaction>> getTransactions(final LocalDate dateFrom,
      final LocalDate dateUntil,
      final Optional<String> bankAccountName) {
    return null;
  }


  @Override
  public ResponseEntity<Void> updateTransaction(final UUID transactionId,
      final Transaction transaction) {
    return null;
  }
}
