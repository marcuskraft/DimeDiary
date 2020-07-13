package com.dimediary.rest.converter;

import com.dimediary.domain.BankAccount;
import com.dimediary.openapi.model.Bankaccount;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BankaccountConverter {

  BankaccountConverter INSTANCE = Mappers.getMapper(
      BankaccountConverter.class);

  BankAccount to(Bankaccount bankaccount);

  Bankaccount from(BankAccount bankAccount);

}
