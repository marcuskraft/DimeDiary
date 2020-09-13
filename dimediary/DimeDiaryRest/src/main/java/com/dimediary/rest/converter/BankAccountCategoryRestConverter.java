package com.dimediary.rest.converter;

import com.dimediary.openapi.model.BankAccountCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BankAccountCategoryRestConverter {

  BankAccountCategory from(com.dimediary.domain.BankAccountCategory bankAccountCategory);

  com.dimediary.domain.BankAccountCategory to(BankAccountCategory bankAccountCategory);

}
