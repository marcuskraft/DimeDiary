package com.dimediary.rest.converter;

import com.dimediary.openapi.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryRestConverter {

  Category from(com.dimediary.domain.Category category);

  com.dimediary.domain.Category to(Category category);

}
