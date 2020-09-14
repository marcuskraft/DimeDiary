package com.dimediary.services;

import com.dimediary.domain.BankAccountCategory;
import com.dimediary.port.in.BankAccountCategoryProvider;
import com.dimediary.port.out.BankAccountCategoryRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankAccountCategoryProviderImpl implements BankAccountCategoryProvider {


  private final BankAccountCategoryRepo bankaccountCategoryService;

  @Autowired
  public BankAccountCategoryProviderImpl(
      final BankAccountCategoryRepo bankaccountCategoryService) {
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
  public void deleteBankAccountCategory(final String bankAccountCategoryName) {
    this.bankaccountCategoryService.delete(bankAccountCategoryName);
  }

  @Override
  public BankAccountCategory persist(final BankAccountCategory bankAccountCategory) {
    return this.bankaccountCategoryService.persist(bankAccountCategory);
  }

  @Override
  public List<BankAccountCategory> getBankAccountCategories() {
    return this.bankaccountCategoryService.getBankAccountCategories();
  }

}
