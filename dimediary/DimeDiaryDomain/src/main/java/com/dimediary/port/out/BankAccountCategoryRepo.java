package com.dimediary.port.out;

import com.dimediary.domain.BankAccountCategory;
import java.util.List;
import java.util.UUID;

public interface BankAccountCategoryRepo {

  List<String> getBankAccountCategoryNames();

  BankAccountCategory getBankAccountCategory(String bankAccountCategoryName);

  List<BankAccountCategory> getBankAccountCategories(List<String> bankAccountCategoryNames);

  List<BankAccountCategory> getBankAccountCategories();

  void deleteBankAccountCategories(List<String> bankAccountCategoryNames);

  void delete(BankAccountCategory bankAccountCategory);

  BankAccountCategory persist(BankAccountCategory bankAccountCategory);

  void delete(UUID bankAccountCategoryId);
}