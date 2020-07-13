package com.dimediary.model.converter;

import com.dimediary.domain.BankAccountCategory;
import com.dimediary.model.entities.BankAccountCategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BankaccountCategoryTransformer {

  BankaccountCategoryTransformer INSTANCE = Mappers.getMapper(BankaccountCategoryTransformer.class);

  BankAccountCategoryEntity bankAccountCategoryToBankaccountCategoryEntity(
      BankAccountCategory bankAccountCategory);

  BankAccountCategory bankAccountCategoryEntityToBankAccountCategory(
      BankAccountCategoryEntity bankAccountCategoryEntity);
}
