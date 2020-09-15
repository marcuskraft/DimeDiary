package com.dimediary.port.in;

import com.dimediary.domain.BankAccountCategory;
import java.util.List;
import java.util.UUID;

public interface BankAccountCategoryProvider {

  List<String> getBankAccountCategoryNames();

  BankAccountCategory getBankAccountCategory(String bankaccountCategoryName);

  void deleteBankAccountCategories(List<String> bankAccountCategoryNames);

  void deleteBankAccountCategory(UUID bankAccountCategoryId);

  BankAccountCategory persist(BankAccountCategory bankAccountCategory);

  List<BankAccountCategory> getBankAccountCategories();
}