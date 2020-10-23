package com.dimediary.persistence.converter;

import com.dimediary.domain.BankAccount;
import com.dimediary.persistence.entities.BankAccountEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UUIDTransformer.class})
public interface BankAccountTransformer {

  BankAccountEntity bankAccountToBankAccountEntity(
      BankAccount bankAccount);

  BankAccount bankAccountEntityToBankAccount(BankAccountEntity bankAccountEntity);

}
