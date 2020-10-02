package com.dimediary.rest.controller;

import com.dimediary.domain.BankAccount;
import com.dimediary.openapi.api.BankAccountApi;
import com.dimediary.port.in.BankAccountProvider;
import com.dimediary.rest.controller.helper.ResponseFactory;
import com.dimediary.rest.converter.BankAccountRestConverter;
import io.swagger.annotations.Api;
import java.util.List;
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
@Api(tags = "BankAccount")
public class BankAccountController implements BankAccountApi {

  private final BankAccountProvider bankAccountProvider;
  private final BankAccountRestConverter bankAccountConverter;
  private final ResponseFactory responseFactory;

  @Autowired
  public BankAccountController(final BankAccountProvider bankAccountProvider,
      final BankAccountRestConverter bankAccountConverter,
      final ResponseFactory responseFactory) {
    this.bankAccountProvider = bankAccountProvider;
    this.bankAccountConverter = bankAccountConverter;
    this.responseFactory = responseFactory;
  }


  @Override
  public ResponseEntity<com.dimediary.openapi.model.BankAccount> saveBankAccount(
      final com.dimediary.openapi.model.BankAccount bankAccount) {
    final BankAccount bankAccountPersisted = this.bankAccountProvider
        .persist(this.bankAccountConverter.to(bankAccount));
    return this.responseFactory.created(this.bankAccountConverter.from(bankAccountPersisted));
  }

  @Override
  public ResponseEntity<Void> deleteBankAccount(final UUID bankAccountId) {
    this.bankAccountProvider.deleteBankAccount(bankAccountId);
    return this.responseFactory.okNoContent();
  }

  @Override
  public ResponseEntity<com.dimediary.openapi.model.BankAccount> getBankAccount(
      final UUID bankAccountId) {
    return this.responseFactory
        .ok(this.bankAccountConverter
            .from(this.bankAccountProvider.getBankAccount(bankAccountId)));
  }


  @Override
  public ResponseEntity<List<com.dimediary.openapi.model.BankAccount>> getBankAccounts() {
    return this.responseFactory.ok((this.bankAccountProvider.getBankAccounts().stream()
        .map(this.bankAccountConverter::from).collect(Collectors.toList())));
  }


}
