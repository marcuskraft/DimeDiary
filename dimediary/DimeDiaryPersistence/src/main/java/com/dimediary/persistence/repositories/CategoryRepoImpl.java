package com.dimediary.persistence.repositories;

import com.dimediary.domain.Category;
import com.dimediary.persistence.converter.CategoryTransformer;
import com.dimediary.persistence.entities.CategoryEntity;
import com.dimediary.persistence.repositories.cruds.CategoryCrudRepository;
import com.dimediary.port.out.CategoryRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
class CategoryRepoImpl implements CategoryRepo {

  private final CategoryCrudRepository categoryCrudRepository;
  private final CategoryTransformer categoryTransformer;

  @Autowired
  public CategoryRepoImpl(final CategoryCrudRepository categoryCrudRepository,
      final CategoryTransformer categoryTransformer) {
    this.categoryCrudRepository = categoryCrudRepository;
    this.categoryTransformer = categoryTransformer;
  }


  @Override
  public void delete(final UUID categoryId) {
    this.categoryCrudRepository.deleteById(categoryId.toString());
  }

  @Override
  public List<Category> getCategories() {
    final List<Category> categories = new ArrayList<>();

    this.categoryCrudRepository.findAll().forEach(categoryEntity -> categories
        .add(this.categoryTransformer.categoryEntityToCategory(categoryEntity)));

    return categories;
  }

  @Override
  public Category persist(final Category category) {
    if (category == null) {
      return null;
    }
    CategoryRepoImpl.log.info("persist Category: " + category.getName());

    if (category.getId() == null) {
      category.setId(UUID.randomUUID());
    }

    CategoryEntity categoryEntity = this.categoryTransformer.categoryToCategoryEntity(category);
    categoryEntity = this.categoryCrudRepository.save(categoryEntity);
    return this.categoryTransformer.categoryEntityToCategory(categoryEntity);
  }


}
