package com.dimediary.persistence.converter;

import com.dimediary.domain.Category;
import com.dimediary.persistence.entities.CategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UUIDTransformer.class})
public interface CategoryTransformer {

  CategoryEntity categoryToCategoryEntity(Category category);

  Category categoryEntityToCategory(CategoryEntity categoryEntity);

}
