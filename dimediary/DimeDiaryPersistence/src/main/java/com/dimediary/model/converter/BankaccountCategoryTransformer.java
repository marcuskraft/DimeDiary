package com.dimediary.model.converter;

import com.dimediary.domain.BankAccountCategory;
import com.dimediary.model.entities.BankAccountCategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BankaccountCategoryTransformer {


  BankAccountCategoryEntity bankAccountCategoryToBankaccountCategoryEntity(
      BankAccountCategory bankAccountCategory);

  BankAccountCategory bankAccountCategoryEntityToBankAccountCategory(
      BankAccountCategoryEntity bankAccountCategoryEntity);
}
