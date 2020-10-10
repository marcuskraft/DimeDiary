package com.dimediary.rest.controller;

import com.dimediary.openapi.api.TransactionApi;
import com.dimediary.openapi.model.Transaction;
import com.dimediary.port.in.TransactionProvider;
import com.dimediary.rest.controller.helper.ResponseFactory;
import com.dimediary.rest.converter.LocalDateConverter;
import com.dimediary.rest.converter.TransactionRestConverter;
import io.swagger.annotations.Api;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping("/api")
@Controller
@Api(tags = "Transaction")
public class TransactionController implements TransactionApi {


  private final TransactionProvider transactionProvider;
  private final TransactionRestConverter transactionRestConverter;
  private final ResponseFactory responseFactory;

  @Autowired
  public TransactionController(final TransactionProvider transactionProvider,
      final TransactionRestConverter transactionRestConverter,
      final ResponseFactory responseFactory) {
    this.transactionProvider = transactionProvider;
    this.transactionRestConverter = transactionRestConverter;
    this.responseFactory = responseFactory;
  }


  @Override
  public ResponseEntity<Transaction> saveTransaction(final Transaction transaction) {
    return this.responseFactory.created(this.transactionRestConverter
        .from(this.transactionProvider
            .persistTransaction(this.transactionRestConverter.to(transaction))));
  }

  @Override
  public ResponseEntity<Void> deleteTransaction(final UUID transactionId) {
    this.transactionProvider.delete(transactionId);
    return this.responseFactory.okNoContent();
  }

  @Override
  public ResponseEntity<Transaction> getTransaction(final UUID transactionId) {
    return this.responseFactory.ok(this.transactionRestConverter
        .from(this.transactionProvider.getTransaction(transactionId)));
  }

  @Override
  public ResponseEntity<List<Transaction>> getTransactions(final String dateFromString,
      final String dateUntilString,
      final Optional<UUID> bankAccountName) {
    final LocalDate dateFrom;
    final LocalDate dateUntil;
    try {
      dateFrom = LocalDateConverter.isoStringToLocalDate(dateFromString);
      dateUntil = LocalDateConverter.isoStringToLocalDate(dateUntilString);
    } catch (final DateTimeParseException e) {
      return ResponseEntity.badRequest().build();
    }
    final List<com.dimediary.domain.Transaction> transactions;
    if (bankAccountName.isPresent()) {
      transactions = this.transactionProvider
          .getTransactions(dateFrom, dateUntil, bankAccountName.get());
    } else {
      transactions = this.transactionProvider.getTransactionsWithoutAccount(dateFrom, dateUntil);
    }

    return this.responseFactory.ok(transactions.stream().map(this.transactionRestConverter::from)
        .collect(Collectors.toList()));
  }


}
