package com.dimediary.rest.controller;

import com.dimediary.domain.BankAccount;
import com.dimediary.openapi.api.BankAccountApi;
import com.dimediary.port.in.BankAccountProvider;
import com.dimediary.rest.controller.helper.ResponseFactory;
import com.dimediary.rest.converter.BankAccountRestConverter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping("/api")
@Controller
public class BankAccountController implements BankAccountApi {

  private static final long serialVersionUID = 395035858890439438L;
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
  public ResponseEntity<com.dimediary.openapi.model.BankAccount> createBankAccount(
      final com.dimediary.openapi.model.BankAccount bankAccount) {
    final BankAccount bankAccountPersisted = this.bankAccountProvider
        .persist(this.bankAccountConverter.to(bankAccount));
    return this.responseFactory.created(this.bankAccountConverter.from(bankAccountPersisted));
  }

  @Override
  public ResponseEntity<Void> deleteBankAccount(final String bankAccountName) {
    this.bankAccountProvider.deleteBankAccount(bankAccountName);
    return this.responseFactory.okNoContent();
  }

  @Override
  public ResponseEntity<com.dimediary.openapi.model.BankAccount> getBankAccount(
      final String bankAccountName) {
    return this.responseFactory
        .ok(this.bankAccountConverter.from(this.bankAccountProvider.getBankAccount(bankAccountName)));
  }


  @Override
  public ResponseEntity<List<String>> getBankAccounts() {
    return this.responseFactory.ok((this.bankAccountProvider.getBankAccountNames()));
  }

  @Override
  public ResponseEntity<Void> updateBankAccount(final String bankAccountName,
      final com.dimediary.openapi.model.BankAccount bankAccount) {
    if (!bankAccountName.equals(bankAccount
        .getName())) { // TODO: we need a id for bank account so that we can also change the name properly
      return this.responseFactory.badRequest();
    }
    this.bankAccountProvider
        .persist(this.bankAccountConverter.to(bankAccount));
    return this.responseFactory.okNoContent();
  }


}
