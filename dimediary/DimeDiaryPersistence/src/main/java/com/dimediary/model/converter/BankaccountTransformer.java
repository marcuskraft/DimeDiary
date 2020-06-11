package com.dimediary.model.converter;

import com.dimediary.domain.BankAccount;
import com.dimediary.model.entities.BankAccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BankaccountTransformer {

  com.dimediary.model.converter.BankaccountTransformer INSTANCE = Mappers.getMapper(
      com.dimediary.model.converter.BankaccountTransformer.class);

  com.dimediary.model.entities.BankAccountEntity bankAccountToBankAccountEntity(
      BankAccount bankAccount);

  @Mapping(source = "bankAccountCategoryEntity", target = "bankAccountCategory")
  @Mapping(source = "bankAccount.name", target = "name")
  BankAccountEntity bankAccountToBankAccountEntity(BankAccount bankAccount,
      com.dimediary.model.entities.BankAccountCategoryEntity bankAccountCategoryEntity);

  BankAccount bankAccountEntityToBankaccount(
      com.dimediary.model.entities.BankAccountEntity bankAccountEntity);

}
