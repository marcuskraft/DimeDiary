package com.dimediary.model.converter;

import com.dimediary.domain.Category;
import com.dimediary.model.entities.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryTransformer {

  CategoryTransformer INSTANCE = Mappers.getMapper(CategoryTransformer.class);

  CategoryEntity categoryToCategoryEntity(Category category);

  Category categoryEntityToCategory(CategoryEntity categoryEntity);

}
