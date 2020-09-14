package com.dimediary.port.in;

import com.dimediary.domain.BankAccountCategory;
import java.util.List;

public interface BankAccountCategoryProvider {

  List<String> getBankAccountCategoryNames();

  BankAccountCategory getBankAccountCategory(String bankaccountCategoryName);

  void deleteBankAccountCategories(List<String> bankAccountCategoryNames);

  void deleteBankAccountCategory(String bankAccountCategoryName);

  BankAccountCategory persist(BankAccountCategory bankAccountCategory);

  List<BankAccountCategory> getBankAccountCategories();
}