package com.dimediary.model.converter;

import com.dimediary.domain.BankAccountCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BankaccountCategoryTransformer {

  com.dimediary.model.converter.BankaccountCategoryTransformer INSTANCE = Mappers.getMapper(
      com.dimediary.model.converter.BankaccountCategoryTransformer.class);

  com.dimediary.model.entities.BankAccountCategoryEntity bankAccountCategoryToBankaccountCategoryEntity(
      BankAccountCategory bankAccountCategory);

  BankAccountCategory bankAccountCategoryEntityToBankAccountCategory(
      com.dimediary.model.entities.BankAccountCategoryEntity bankAccountCategoryEntity);
}
