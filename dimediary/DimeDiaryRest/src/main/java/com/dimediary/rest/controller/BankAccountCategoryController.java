package com.dimediary.rest.controller;

import com.dimediary.openapi.api.BankAccountCategoryApi;
import com.dimediary.openapi.model.BankAccountCategory;
import com.dimediary.port.in.BankAccountCategoryProvider;
import com.dimediary.rest.controller.helper.ResponseFactory;
import com.dimediary.rest.converter.BankAccountCategoryRestConverter;
import io.swagger.annotations.Api;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping("/api")
@Controller
@Api(tags = "BankAccountCategory")
public class BankAccountCategoryController implements BankAccountCategoryApi {

  private final BankAccountCategoryProvider bankaccountCategoryProvider;
  private final BankAccountCategoryRestConverter bankAccountCategoryRestConverter;
  private final ResponseFactory responseFactory;

  public BankAccountCategoryController(
      final BankAccountCategoryProvider bankaccountCategoryProvider,
      final BankAccountCategoryRestConverter bankAccountCategoryRestConverter,
      final ResponseFactory responseFactory) {
    this.bankaccountCategoryProvider = bankaccountCategoryProvider;
    this.bankAccountCategoryRestConverter = bankAccountCategoryRestConverter;
    this.responseFactory = responseFactory;
  }


  @Override
  public ResponseEntity<BankAccountCategory> createBankAccountCategory(
      final BankAccountCategory bankAccountCategory) {
    return this.responseFactory.created(this.bankAccountCategoryRestConverter
        .from(this.bankaccountCategoryProvider
            .persist(this.bankAccountCategoryRestConverter.to(bankAccountCategory))));
  }

  @Override
  public ResponseEntity<Void> deleteBankAccountCategory(final String bankAccountCategoryName) {
    this.bankaccountCategoryProvider.deleteBankAccountCategory(bankAccountCategoryName);
    return this.responseFactory.okNoContent();
  }

  @Override
  public ResponseEntity<List<BankAccountCategory>> getBankAccountCategories() {
    return this.responseFactory
        .ok(this.bankaccountCategoryProvider.getBankAccountCategories().stream()
            .map(this.bankAccountCategoryRestConverter::from).collect(
                Collectors.toList()));
  }

  @Override
  public ResponseEntity<Void> updateBankAccountCategory(final String bankAccountCategoryName,
      final BankAccountCategory bankAccountCategory) {
    if (!bankAccountCategoryName.equals(bankAccountCategory.getName())) {
      return this.responseFactory.badRequest();
    }
    this.bankaccountCategoryProvider
        .persist(this.bankAccountCategoryRestConverter.to(bankAccountCategory));
    return this.responseFactory.okNoContent();
  }


}
