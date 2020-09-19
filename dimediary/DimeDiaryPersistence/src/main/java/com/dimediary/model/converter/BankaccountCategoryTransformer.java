package com.dimediary.model.converter;

import com.dimediary.domain.BankAccountCategory;
import com.dimediary.model.entities.BankAccountCategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UUIDTransformer.class})
public interface BankAccountCategoryTransformer {


  BankAccountCategoryEntity bankAccountCategoryToBankAccountCategoryEntity(
      BankAccountCategory bankAccountCategory);

  BankAccountCategory bankAccountCategoryEntityToBankAccountCategory(
      BankAccountCategoryEntity bankAccountCategoryEntity);
}
