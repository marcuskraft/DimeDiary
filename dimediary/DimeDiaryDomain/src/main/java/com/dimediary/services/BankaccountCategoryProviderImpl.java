package com.dimediary.services;

import com.dimediary.domain.BankAccountCategory;
import com.dimediary.port.in.BankaccountCategoryProvider;
import com.dimediary.port.out.BankaccountCategoryRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankaccountCategoryProviderImpl implements BankaccountCategoryProvider {


  private final BankaccountCategoryRepo bankaccountCategoryService;

  @Autowired
  public BankaccountCategoryProviderImpl(
      final BankaccountCategoryRepo bankaccountCategoryService) {
    this.bankaccountCategoryService = bankaccountCategoryService;
  }

  @Override
  public List<String> getBankAccountCategoryNames() {
    return this.bankaccountCategoryService.getBankAccountCategoryNames();
  }

  @Override
  public BankAccountCategory getBankAccountCategory(final String bankaccountCategoryName) {
    return this.bankaccountCategoryService.getBankAccountCategory(bankaccountCategoryName);
  }

  @Override
  public void deleteBankAccountCategories(final List<String> bankAccountCategoryNames) {
    this.bankaccountCategoryService.deleteBankAccountCategories(bankAccountCategoryNames);
  }

  @Override
  public void persist(final BankAccountCategory bankAccountCategory) {
    this.bankaccountCategoryService.persist(bankAccountCategory);
  }

}
