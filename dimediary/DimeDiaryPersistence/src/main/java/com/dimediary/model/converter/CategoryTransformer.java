package com.dimediary.model.converter;

import com.dimediary.domain.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryTransformer {

  com.dimediary.model.converter.CategoryTransformer INSTANCE = Mappers.getMapper(
      com.dimediary.model.converter.CategoryTransformer.class);

  com.dimediary.model.entities.CategoryEntity categoryToCategoryEntity(Category category);

  Category categoryEntityToCategory(com.dimediary.model.entities.CategoryEntity categoryEntity);

}
