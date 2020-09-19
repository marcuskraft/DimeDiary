package com.dimediary.services;

import com.dimediary.domain.Category;
import com.dimediary.port.in.CategoryProvider;
import com.dimediary.port.out.CategoryRepo;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryProviderImpl implements CategoryProvider {

  private final CategoryRepo categoryService;

  @Autowired
  public CategoryProviderImpl(final CategoryRepo categoryService) {
    this.categoryService = categoryService;
  }

  @Override
  public List<Category> getCategories() {
    return this.categoryService.getCategories();
  }

  @Override
  public Category persist(final Category category) {
    return this.categoryService.persist(category);
  }

  @Override
  public void deleteCategory(final UUID categoryId) {
    this.categoryService.delete(categoryId);
  }

}
