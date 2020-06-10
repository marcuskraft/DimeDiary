package com.dimediary.services;

import com.dimediary.domain.BankAccountCategory;
import com.dimediary.port.in.BankaccountCategoryProvider;
import com.dimediary.port.out.BankaccountCategoryRepo;
import java.util.List;
import javax.inject.Inject;


public class BankaccountCategoryProviderImpl implements BankaccountCategoryProvider {

  @Inject
  private BankaccountCategoryRepo bankaccountCategoryService;

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
