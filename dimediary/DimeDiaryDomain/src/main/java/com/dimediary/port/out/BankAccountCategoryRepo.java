package com.dimediary.port.out;

import com.dimediary.domain.BankAccountCategory;
import java.util.List;
import java.util.UUID;

public interface BankAccountCategoryRepo {

  List<BankAccountCategory> getBankAccountCategories();

  BankAccountCategory persist(BankAccountCategory bankAccountCategory);

  void delete(UUID bankAccountCategoryId);
}