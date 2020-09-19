package com.dimediary.port.out;

import com.dimediary.domain.Category;
import java.util.List;
import java.util.UUID;

public interface CategoryRepo {
  
  Category persist(Category category);

  void delete(UUID categoryId);

  List<Category> getCategories();
}