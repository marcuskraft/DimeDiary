package com.dimediary.rest.controller;

import com.dimediary.domain.BankAccount;
import com.dimediary.openapi.api.BankaccountApi;
import com.dimediary.openapi.model.Bankaccount;
import com.dimediary.openapi.model.BankaccountNames;
import com.dimediary.port.in.BankAccountProvider;
import com.dimediary.rest.converter.BankaccountConverter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping("/api")
@Controller
public class BankaccountController implements BankaccountApi {

  private final BankAccountProvider bankAccountProvider;

  private final BankaccountConverter bankaccountConverter;

  @Autowired
  public BankaccountController(final BankAccountProvider bankAccountProvider,
      final BankaccountConverter bankaccountConverter) {
    this.bankAccountProvider = bankAccountProvider;
    this.bankaccountConverter = bankaccountConverter;
  }


  @Override
  public ResponseEntity<BankaccountNames> bankaccountGet() {
    final List<String> bankAccountNames = this.bankAccountProvider.getBankAccountNames();

    final BankaccountNames bankaccountNamesList = new BankaccountNames();

    bankAccountNames.forEach(s -> {
      bankaccountNamesList.addBankaccountNamesItem(s);
    });

    return ResponseEntity.ok().body(bankaccountNamesList);

  }

  @Override
  public ResponseEntity<Bankaccount> bankaccountPost(final Bankaccount bankaccount) {

    this.bankAccountProvider.persist(this.bankaccountConverter.to(bankaccount));

    final BankAccount bankAccount = this.bankAccountProvider.getBankAccount(bankaccount.getName());
    final Bankaccount bankaccountResp = this.bankaccountConverter.from(bankAccount);

    return ResponseEntity.ok(bankaccountResp);

  }
}
