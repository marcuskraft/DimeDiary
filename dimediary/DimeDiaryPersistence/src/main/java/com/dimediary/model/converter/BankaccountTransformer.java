package com.dimediary.model.converter;

import com.dimediary.domain.BankAccount;
import com.dimediary.model.entities.BankAccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BankaccountTransformer {

  @Mapping(target = "dateStartBudget", source = "dateStartBalance")
  @Mapping(target = "startBudget", source = "startBalance")
  BankAccountEntity bankAccountToBankAccountEntity(
      BankAccount bankAccount);

  @Mapping(target = "dateStartBalance", source = "dateStartBudget")
  @Mapping(target = "startBalance", source = "startBudget")
  BankAccount bankAccountEntityToBankAccount(BankAccountEntity bankAccountEntity);

}
