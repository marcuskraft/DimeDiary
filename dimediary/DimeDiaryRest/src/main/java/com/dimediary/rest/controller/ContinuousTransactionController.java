package com.dimediary.rest.controller;

import com.dimediary.openapi.api.ContinuousTransactionApi;
import com.dimediary.openapi.model.ContinuousTransaction;
import com.dimediary.openapi.model.DayOfWeekAPI;
import com.dimediary.openapi.model.RecurrenceSettings;
import com.dimediary.openapi.model.RecurrenceType;
import com.dimediary.port.in.ContinuousTransactionProvider;
import com.dimediary.rest.controller.helper.ResponseFactory;
import com.dimediary.rest.converter.ContinuousTransactionRestConverter;
import com.dimediary.rest.converter.LocalDateConverter;
import io.swagger.annotations.Api;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
          .created(this.continuousTransactionRestConverter.from(this.continuousTransactionProvider
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
    return this.responseFactory.ok(this.continuousTransactionRestConverter.from(
        this.continuousTransactionProvider.getContinuousTransactions(continuousTransactionId)));
  }

  @Override
  public ResponseEntity<List<String>> getRecurrenceDates(final String dateBegin,
      final RecurrenceType recurrenceType, final Integer interval,
      final Optional<Integer> dayOfMonth,
      final Optional<Boolean> isDayOfMonthFromBehind, final Optional<List<DayOfWeekAPI>> dayOfWeeks,
      final Optional<String> until, final Optional<Integer> count) {
    final ContinuousTransaction continuousTransaction = this.createContinuousTransaction(
        dateBegin, recurrenceType, interval, dayOfMonth, isDayOfMonthFromBehind, dayOfWeeks, until,
        count);

    return this.responseFactory.ok(this.continuousTransactionProvider
        .getRecurrenceDates(this.continuousTransactionRestConverter.to(continuousTransaction))
        .stream().map(LocalDateConverter::localDateToIsoString).collect(Collectors.toList()));
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
        .map(this.continuousTransactionRestConverter::from).collect(Collectors.toList()));
  }

  private ContinuousTransaction createContinuousTransaction(final String dateBegin,
      final RecurrenceType recurrenceType, final Integer interval,
      final Optional<Integer> dayOfMonth,
      final Optional<Boolean> isDayOfMonthFromBehind, final Optional<List<DayOfWeekAPI>> dayOfWeeks,
      final Optional<String> until, final Optional<Integer> count) {
    final ContinuousTransaction continuousTransaction = new ContinuousTransaction();
    final RecurrenceSettings recurrenceSettings = this.createRecurrenceSettings(
        recurrenceType, interval, dayOfMonth, isDayOfMonthFromBehind, dayOfWeeks, until, count);
    continuousTransaction.setDateBegin(dateBegin);
    continuousTransaction.setRecurrenceSettings(recurrenceSettings);
    continuousTransaction
        .setRecurrenceExceptions(Collections.emptyList());
    continuousTransaction
        .setRecurrenceExtraInstances(Collections.emptyList());
    return continuousTransaction;
  }


  private RecurrenceSettings createRecurrenceSettings(final RecurrenceType recurrenceType,
      final Integer interval, final Optional<Integer> dayOfMonth,
      final Optional<Boolean> isDayOfMonthFromBehind,
      final Optional<List<DayOfWeekAPI>> dayOfWeeks, final Optional<String> until,
      final Optional<Integer> count) {
    final RecurrenceSettings recurrenceSettings = new RecurrenceSettings();
    recurrenceSettings.setDayOfWeeks(dayOfWeeks.orElse(Collections.emptyList()));
    recurrenceSettings.setCount(count.orElse(null));
    recurrenceSettings.setDayOfMonth(dayOfMonth.orElse(null));
    recurrenceSettings.setInterval(interval);
    recurrenceSettings.setRecurrenceType(recurrenceType);
    recurrenceSettings.setIsDayOfMonthFromBehind(isDayOfMonthFromBehind.orElse(false));
    recurrenceSettings.setUntil(until.orElse(null));
    return recurrenceSettings;
  }


}
