package com.dimediary.port.in;

import com.dimediary.domain.BankAccountCategory;
import java.util.List;
import java.util.UUID;

public interface BankAccountCategoryProvider {

  void deleteBankAccountCategory(UUID bankAccountCategoryId);

  BankAccountCategory persist(BankAccountCategory bankAccountCategory);

  List<BankAccountCategory> getBankAccountCategories();
}