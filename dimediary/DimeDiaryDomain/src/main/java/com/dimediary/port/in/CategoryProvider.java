package com.dimediary.port.in;

import com.dimediary.domain.BankAccount;
import com.dimediary.domain.Category;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface CategoryProvider {

  Category getCategory(final String categoryName);

  Map<Category, Double> calculatePercentageFromTo(final BankAccount bankAccount,
      final LocalDate from,
      final LocalDate to);

  List<Category> getCategories(ArrayList<String> categoryNames);

  List<String> getCategoryNames();

  void persist(Category category);

  void deleteCategories(List<Category> categories);

}
