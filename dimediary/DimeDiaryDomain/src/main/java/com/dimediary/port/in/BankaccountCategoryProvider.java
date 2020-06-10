package com.dimediary.port.in;

import com.dimediary.domain.BankAccountCategory;
import java.util.List;

public interface BankaccountCategoryProvider {

  List<String> getBankAccountCategoryNames();

  BankAccountCategory getBankAccountCategory(String bankaccountCategoryName);

  void deleteBankAccountCategories(List<String> bankAccountCategoryNames);

  void persist(BankAccountCategory bankAccountCategory);

}