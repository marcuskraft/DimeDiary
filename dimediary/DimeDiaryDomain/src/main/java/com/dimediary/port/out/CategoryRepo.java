package com.dimediary.port.out;

import com.dimediary.domain.Category;
import java.util.List;

public interface CategoryRepo {

  Category getCategory(String categoryName);

  List<Category> getCategories(List<String> categoryNames);

  List<String> getCategoryNames();

  Category persist(Category category);

  void delete(Category category);

  void deleteCategories(List<Category> categories);

  void delete(String categoryId);

  List<Category> getCategories();
}