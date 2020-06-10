package com.dimediary.port.out;

import com.dimediary.domain.BankAccountCategory;
import java.util.List;

public interface BankaccountCategoryRepo {

  List<String> getBankAccountCategoryNames();

  BankAccountCategory getBankAccountCategory(String bankAccountCategoryName);

  List<BankAccountCategory> getBankAccountCategories(List<String> bankAccountCategoryNames);

  void deleteBankAccountCategories(List<String> bankAccountCategoryNames);

  void delete(BankAccountCategory bankAccountCategory);

  void persist(BankAccountCategory bankAccountCategory);

}