package com.dimediary.model.repositories;

import com.dimediary.domain.Category;
import com.dimediary.model.converter.CategoryTransformer;
import com.dimediary.model.entities.CategoryEntity;
import com.dimediary.port.out.CategoryRepo;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
class CategoryRepoImpl implements CategoryRepo {

  @PersistenceContext
  private final EntityManager entityManager;

  private final CategoryTransformer categoryTransformer;

  @Autowired
  public CategoryRepoImpl(final EntityManager entityManager,
      final CategoryTransformer categoryTransformer) {
    this.entityManager = entityManager;
    this.categoryTransformer = categoryTransformer;
  }

  @Override
  public Category getCategory(final String categoryName) {
    Validate.notEmpty(categoryName);

    log.info("getCategory: " + categoryName);
    final com.dimediary.model.entities.CategoryEntity categoryEntity = this.entityManager.find(
        com.dimediary.model.entities.CategoryEntity.class,
        categoryName);
    return this.entityToDomain(categoryEntity);
  }

  @Override
  public java.util.List<Category> getCategories(final List<String> categoryNames) {
    Validate.notNull(categoryNames);
    Validate.notEmpty(categoryNames);

    final java.util.List<com.dimediary.model.entities.CategoryEntity> categoryEntities = this.entityManager
        .createNamedQuery("findCategories", com.dimediary.model.entities.CategoryEntity.class)
        .setParameter("namesList", categoryNames)
        .getResultList();
    return this.entitiesToDomains(categoryEntities);
  }

  @Override
  public java.util.List<String> getCategoryNames() {
    com.dimediary.model.repositories.CategoryRepoImpl.log.info("getCategoryNames");
    final java.util.List<String> categoryNames = new java.util.ArrayList<>();

    final java.util.List<com.dimediary.model.entities.CategoryEntity> categories = this.entityManager
        .createNamedQuery("allCategories", com.dimediary.model.entities.CategoryEntity.class)
        .getResultList();

    for (final com.dimediary.model.entities.CategoryEntity category : categories) {
      categoryNames.add(category.getName());
    }

    return categoryNames;
  }

  @Override
  public Category persist(final Category category) {
    if (category == null) {
      return null;
    }
    com.dimediary.model.repositories.CategoryRepoImpl.log
        .info("persist Category: " + category.getName());

    com.dimediary.model.entities.CategoryEntity categoryEntity = this.categoryTransformer
        .categoryToCategoryEntity(category);

    if (this.findEntity(category) != null) {
      categoryEntity = this.entityManager.merge(categoryEntity);

    } else {
      this.entityManager.persist(categoryEntity);
    }

    return this.categoryTransformer.categoryEntityToCategory(categoryEntity);

  }

  @Override
  public void delete(final Category category) {
    Validate.notNull(category);
    try {
      com.dimediary.model.repositories.CategoryRepoImpl.log
          .info("delete Category: " + category.getName());

      final com.dimediary.model.entities.CategoryEntity categoryEntity = this.findEntity(category);

      if (categoryEntity != null) {
        this.entityManager.remove(categoryEntity);
      }

    } catch (final Exception e) {
      com.dimediary.model.repositories.CategoryRepoImpl.log
          .error("can't delete category: " + category.getName(), e);
      throw e;
    }
  }

  @Override
  public void deleteCategories(final List<Category> categories) {
    Validate.notNull(categories);

    for (final Category category : categories) {
      this.delete(category);
    }

  }

  @Override
  public void delete(final String categoryName) {
    this.delete(this.getCategory(categoryName));
  }

  @Override
  public List<Category> getCategories() {
    final List<String> categoryNames = this.getCategoryNames();
    return this.getCategories(categoryNames);
  }

  private Category entityToDomain(final CategoryEntity categoryEntity) {
    return this.categoryTransformer.categoryEntityToCategory(categoryEntity);
  }

  private List<Category> entitiesToDomains(
      final List<CategoryEntity> categoryEntities) {
    return categoryEntities.stream().map(this::entityToDomain)
        .collect(Collectors.toList());
  }

  private CategoryEntity findEntity(final Category category) {
    if (category != null && category.getName() != null) {
      return this.entityManager.find(CategoryEntity.class, category.getName());
    }
    return null;
  }
}
