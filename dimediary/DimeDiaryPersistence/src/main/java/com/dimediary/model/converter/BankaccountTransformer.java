package com.dimediary.model.converter;

import com.dimediary.domain.BankAccount;
import com.dimediary.model.entities.BankAccountCategoryEntity;
import com.dimediary.model.entities.BankAccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BankaccountTransformer {

  BankaccountTransformer INSTANCE = Mappers.getMapper(BankaccountTransformer.class);

  BankAccountEntity bankAccountToBankAccountEntity(
      BankAccount bankAccount);

  @Mapping(source = "bankAccountCategoryEntity", target = "bankAccountCategory")
  @Mapping(source = "bankAccount.name", target = "name")
  BankAccountEntity bankAccountToBankAccountEntity(BankAccount bankAccount,
      BankAccountCategoryEntity bankAccountCategoryEntity);

  BankAccount bankAccountEntityToBankaccount(BankAccountEntity bankAccountEntity);

}
