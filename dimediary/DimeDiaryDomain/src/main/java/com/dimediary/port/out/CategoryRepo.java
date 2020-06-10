package com.dimediary.port.out;

import com.dimediary.domain.Category;
import java.util.ArrayList;
import java.util.List;

public interface CategoryRepo {

  Category getCategory(String categoryName);

  List<Category> getCategories(ArrayList<String> categoryNames);

  List<String> getCategoryNames();

  void persist(Category category);

  void delete(Category category);

  void deleteCategories(List<Category> categories);

}