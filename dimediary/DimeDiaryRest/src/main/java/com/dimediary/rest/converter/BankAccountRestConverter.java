package com.dimediary.rest.converter;

import com.dimediary.domain.BankAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {BankAccountCategoryRestConverter.class})
public interface BankAccountRestConverter {


  BankAccount to(com.dimediary.openapi.model.BankAccount bankaccount);

  com.dimediary.openapi.model.BankAccount from(BankAccount bankAccount);

}
