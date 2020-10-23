package com.dimediary.rest.controller;

import com.dimediary.openapi.api.ContinuousTransactionApi;
import com.dimediary.openapi.model.ContinuousTransaction;
import com.dimediary.port.in.ContinuousTransactionProvider;
import com.dimediary.rest.controller.helper.ResponseFactory;
import com.dimediary.rest.converter.ContinuousTransactionRestConverter;
import com.dimediary.rest.converter.LocalDateConverter;
import io.swagger.annotations.Api;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.server.WebServerException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping("/api")
@Controller
@Api(tags = "ContinuousTransaction")
public class ContinuousTransactionController implements ContinuousTransactionApi {

  private final ContinuousTransactionProvider continuousTransactionProvider;
  private final ContinuousTransactionRestConverter continuousTransactionRestConverter;
  private final ResponseFactory responseFactory;

  public ContinuousTransactionController(
      final ContinuousTransactionProvider continuousTransactionProvider,
      final ContinuousTransactionRestConverter continuousTransactionRestConverter,
      final ResponseFactory responseFactory) {
    this.continuousTransactionProvider = continuousTransactionProvider;
    this.continuousTransactionRestConverter = continuousTransactionRestConverter;
    this.responseFactory = responseFactory;
  }


  @Override
  public ResponseEntity<ContinuousTransaction> saveContinuousTransaction(
      final ContinuousTransaction continuousTransaction) {
    try {
      return this.responseFactory
          .created(ContinuousTransactionRestConverter.from(this.continuousTransactionProvider
              .persist(this.continuousTransactionRestConverter.to(continuousTransaction))));
    } catch (final Exception e) {
      log.error("error during saving continuous transaction", e);
      throw new WebServerException("error during saving continuous transaction", e);
    }
  }

  @Override
  public ResponseEntity<Void> deleteContinuousTransaction(final UUID continuousTransactionId) {
    this.continuousTransactionProvider.deleteAllContinuousTransactions(continuousTransactionId);
    return this.responseFactory.okNoContent();
  }

  @Override
  public ResponseEntity<ContinuousTransaction> getContinuousTransaction(
      final UUID continuousTransactionId) {
    return this.responseFactory.ok(ContinuousTransactionRestConverter.from(
        this.continuousTransactionProvider.getContinuousTransactions(continuousTransactionId)));
  }

  @Override
  public ResponseEntity<List<ContinuousTransaction>> loadContinuousTransaction(
      final UUID bankAccountId, final String dateFrom, final String dateUntil) {
    final LocalDate localDateFrom;
    final LocalDate localDateUntil;
    try {
      localDateFrom = LocalDateConverter.isoStringToLocalDate(dateFrom);
      localDateUntil = LocalDateConverter.isoStringToLocalDate(dateUntil);
    } catch (final DateTimeParseException e) {
      return ResponseEntity.badRequest().build();
    }
    return this.responseFactory.ok(this.continuousTransactionProvider
        .loadContinuousTransactions(bankAccountId, localDateFrom, localDateUntil).stream()
        .map(ContinuousTransactionRestConverter::from).collect(Collectors.toList()));
  }


}
