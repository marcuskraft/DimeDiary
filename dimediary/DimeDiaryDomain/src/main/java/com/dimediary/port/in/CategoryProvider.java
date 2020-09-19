package com.dimediary.port.in;

import com.dimediary.domain.Category;
import java.util.List;
import java.util.UUID;

public interface CategoryProvider {

  List<Category> getCategories();

  Category persist(Category category);

  void deleteCategory(UUID categoryId);

}
