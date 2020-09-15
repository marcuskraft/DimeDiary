package com.dimediary.model.converter;

import com.dimediary.domain.BankAccount;
import com.dimediary.model.entities.BankAccountEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BankaccountTransformer {

  BankAccountEntity bankAccountToBankAccountEntity(
      BankAccount bankAccount);
  
  BankAccount bankAccountEntityToBankAccount(BankAccountEntity bankAccountEntity);

}
