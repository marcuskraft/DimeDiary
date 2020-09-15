package com.dimediary.port.out;

import com.dimediary.domain.Category;
import java.util.List;
import java.util.UUID;

public interface CategoryRepo {

  Category getCategory(UUID categoryId);

  List<Category> getCategories(List<String> categoryNames);

  List<String> getCategoryNames();

  Category persist(Category category);

  void delete(Category category);

  void deleteCategories(List<Category> categories);

  void delete(UUID categoryId);

  List<Category> getCategories();
}