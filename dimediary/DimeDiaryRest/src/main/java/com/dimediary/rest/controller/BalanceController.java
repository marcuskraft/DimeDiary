package com.dimediary.rest.controller;

import com.dimediary.openapi.api.BalanceApi;
import com.dimediary.openapi.model.BalanceHistory;
import com.dimediary.port.in.BalanceProvider;
import com.dimediary.rest.controller.helper.ResponseFactory;
import com.dimediary.rest.converter.BalanceRestConverter;
import io.swagger.annotations.Api;
import java.time.LocalDate;
import java.util.List;
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

  private final BalanceProvider balanceProvider;
  private final BalanceRestConverter balanceRestConverter;
  private final ResponseFactory responseFactory;

  public BalanceController(final BalanceProvider balanceProvider,
      final BalanceRestConverter balanceRestConverter,
      final ResponseFactory responseFactory) {
    this.balanceProvider = balanceProvider;
    this.balanceRestConverter = balanceRestConverter;
    this.responseFactory = responseFactory;
  }


  @Override
  public ResponseEntity<List<BalanceHistory>> getBalances(
      final String bankAccountName, final LocalDate dateFrom,
      final LocalDate dateUntil) {
    return this.responseFactory
        .ok(this.balanceProvider.getBalancesFollowingDays(bankAccountName, dateFrom, dateUntil)
            .stream().map(this.balanceRestConverter::from).collect(
                Collectors.toList()));
  }

  @Override
  public ResponseEntity<Void> recreateBalances(final Integer bankAccountId) {
    return null; // TODO: implement
  }
}
