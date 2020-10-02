package com.dimediary.rest.controller;

import com.dimediary.openapi.api.BalanceApi;
import com.dimediary.openapi.model.Balance;
import com.dimediary.port.in.BalanceUseCase;
import com.dimediary.rest.controller.helper.ResponseFactory;
import com.dimediary.rest.converter.BalanceRestConverter;
import com.dimediary.rest.converter.LocalDateConverter;
import io.swagger.annotations.Api;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping("/api")
@Controller
@Api(tags = "Balance")
public class BalanceController implements BalanceApi {

  private final BalanceUseCase balanceUseCase;
  private final BalanceRestConverter balanceRestConverter;
  private final ResponseFactory responseFactory;

  public BalanceController(final BalanceUseCase balanceUseCase,
      final BalanceRestConverter balanceRestConverter,
      final ResponseFactory responseFactory) {
    this.balanceUseCase = balanceUseCase;
    this.balanceRestConverter = balanceRestConverter;
    this.responseFactory = responseFactory;
  }


  @Override
  public ResponseEntity<List<Balance>> getBalances(
      final UUID bankAccountId, final String dateFromString,
      final String dateUntilString) {
    final LocalDate dateFrom;
    final LocalDate dateUntil;
    try {
      dateFrom = LocalDateConverter.isoStringToLocalDate(dateFromString);
      dateUntil = LocalDateConverter.isoStringToLocalDate(dateUntilString);
    } catch (final DateTimeParseException e) {
      return ResponseEntity.badRequest().build();
    }
    return this.responseFactory
        .ok(this.balanceUseCase.getBalancesFollowingDays(bankAccountId, dateFrom, dateUntil)
            .stream().map(this.balanceRestConverter::from).collect(
                Collectors.toList()));
  }

  @Override
  public ResponseEntity<Void> recreateBalances(final UUID bankAccountId) {
    return null; // TODO: implement
  }
}
