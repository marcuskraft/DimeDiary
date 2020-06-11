package com.dimediary.model.repositories;

import com.dimediary.domain.Category;
import com.dimediary.model.converter.CategoryTransformer;
import com.dimediary.model.repositories.helper.DatabaseTransactionProviderImpl;
import com.dimediary.port.out.CategoryRepo;
import javax.inject.Inject;
import org.apache.commons.lang3.Validate;

class CategoryRepoImpl implements CategoryRepo {

  private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager
      .getLogger(
          com.dimediary.model.repositories.CategoryRepoImpl.class);

  @Inject
  private DatabaseTransactionProviderImpl entityManagerService;

  @Inject
  private CategoryTransformer categoryTransformer;

  @Override
  public Category getCategory(final String categoryName) {
    Validate.notEmpty(categoryName);

    com.dimediary.model.repositories.CategoryRepoImpl.log.info("getCategory: " + categoryName);
    final com.dimediary.model.entities.CategoryEntity categoryEntity = this.entityManagerService
        .getEntityManager().find(
            com.dimediary.model.entities.CategoryEntity.class,
            categoryName);
    return this.entityToDomain(categoryEntity);
  }

  @Override
  public java.util.List<Category> getCategories(final java.util.ArrayList<String> categoryNames) {
    Validate.notNull(categoryNames);
    Validate.notEmpty(categoryNames);

    final java.util.List<com.dimediary.model.entities.CategoryEntity> categoryEntities = this.entityManagerService
        .getEntityManager()
        .createNamedQuery("findCategories", com.dimediary.model.entities.CategoryEntity.class)
        .setParameter("namesList", categoryNames)
        .getResultList();
    return this.entitiesToDomains(categoryEntities);
  }

  @Override
  public java.util.List<String> getCategoryNames() {
    com.dimediary.model.repositories.CategoryRepoImpl.log.info("getCategoryNames");
    final java.util.List<String> categoryNames = new java.util.ArrayList<>();

    final java.util.List<com.dimediary.model.entities.CategoryEntity> categories = this.entityManagerService
        .getEntityManager()
        .createNamedQuery("allCategories", com.dimediary.model.entities.CategoryEntity.class)
        .getResultList();

    for (final com.dimediary.model.entities.CategoryEntity category : categories) {
      categoryNames.add(category.getName());
    }

    return categoryNames;
  }

  @Override
  public void persist(final Category category) {
    if (category == null) {
      return;
    }
    com.dimediary.model.repositories.CategoryRepoImpl.log
        .info("persist Category: " + category.getName());
    final boolean ownTransaction = this.entityManagerService.beginTransaction();

    final com.dimediary.model.entities.CategoryEntity categoryEntity = this.categoryTransformer
        .categoryToCategoryEntity(category);

    if (this.findEntity(category) != null) {
      this.entityManagerService.getEntityManager().merge(categoryEntity);
    } else {
      this.entityManagerService.getEntityManager().persist(categoryEntity);
    }

    if (ownTransaction) {
      this.entityManagerService.commitTransaction();
    }

  }

  @Override
  public void delete(final Category category) {
    Validate.notNull(category);
    try {
      com.dimediary.model.repositories.CategoryRepoImpl.log
          .info("delete Category: " + category.getName());
      final boolean ownTransaction = this.entityManagerService.beginTransaction();

      final com.dimediary.model.entities.CategoryEntity categoryEntity = this.findEntity(category);

      if (categoryEntity != null) {
        this.entityManagerService.getEntityManager().remove(categoryEntity);
      }

      if (ownTransaction) {
        this.entityManagerService.commitTransaction();
      }
    } catch (final Exception e) {
      com.dimediary.model.repositories.CategoryRepoImpl.log
          .error("can't delete category: " + category.getName(), e);
      throw e;
    }
  }

  @Override
  public void deleteCategories(final java.util.List<Category> categories) {
    Validate.notNull(categories);

    final boolean ownTransaction = this.entityManagerService.beginTransaction();

    for (final Category category : categories) {
      this.delete(category);
    }

    if (ownTransaction) {
      this.entityManagerService.commitTransaction();
    }
  }

  private Category entityToDomain(
      final com.dimediary.model.entities.CategoryEntity categoryEntity) {
    return this.categoryTransformer.categoryEntityToCategory(categoryEntity);
  }

  private java.util.List<Category> entitiesToDomains(
      final java.util.List<com.dimediary.model.entities.CategoryEntity> categoryEntities) {
    return categoryEntities.stream().map((categoryEntity) -> this.entityToDomain(categoryEntity))
        .collect(java.util.stream.Collectors.toList());
  }

  private com.dimediary.model.entities.CategoryEntity findEntity(final Category category) {
    if (category != null && category.getName() != null) {
      return this.entityManagerService.getEntityManager().find(
          com.dimediary.model.entities.CategoryEntity.class, category.getName());
    }
    return null;
  }
}
