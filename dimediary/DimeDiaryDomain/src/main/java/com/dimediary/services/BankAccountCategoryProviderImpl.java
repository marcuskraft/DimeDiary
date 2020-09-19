package com.dimediary.services;

import com.dimediary.domain.BankAccountCategory;
import com.dimediary.port.in.BankAccountCategoryProvider;
import com.dimediary.port.out.BankAccountCategoryRepo;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankAccountCategoryProviderImpl implements BankAccountCategoryProvider {


  private final BankAccountCategoryRepo bankAccountCategoryService;

  @Autowired
  public BankAccountCategoryProviderImpl(
      final BankAccountCategoryRepo bankAccountCategoryService) {
    this.bankAccountCategoryService = bankAccountCategoryService;
  }

  @Override
  public void deleteBankAccountCategory(final UUID bankAccountCategoryId) {
    this.bankAccountCategoryService.delete(bankAccountCategoryId);
  }

  @Override
  public BankAccountCategory persist(final BankAccountCategory bankAccountCategory) {
    return this.bankAccountCategoryService.persist(bankAccountCategory);
  }

  @Override
  public List<BankAccountCategory> getBankAccountCategories() {
    return this.bankAccountCategoryService.getBankAccountCategories();
  }

}
