package com.dimediary.services;

import com.dimediary.domain.BankAccount;
import com.dimediary.domain.Category;
import com.dimediary.domain.Transaction;
import com.dimediary.port.in.CategoryProvider;
import com.dimediary.port.in.TransactionProvider;
import com.dimediary.port.out.CategoryRepo;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryProviderImpl implements CategoryProvider {

  private final CategoryRepo categoryService;

  private final TransactionProvider transactionProvider;

  @Autowired
  public CategoryProviderImpl(final CategoryRepo categoryService,
      final TransactionProvider transactionProvider) {
    this.categoryService = categoryService;
    this.transactionProvider = transactionProvider;
  }

  @Override
  public Category getCategory(final String categoryName) {
    return this.categoryService.getCategory(categoryName);
  }

  @Override
  public Map<Category, Double> calculatePercentageFromTo(final BankAccount bankAccount,
      final LocalDate from,
      final LocalDate to) {
    final Map<Category, Double> calculatedPercentage = new HashMap<>();

    final Map<Category, Double> calculatedAbsolute = new HashMap<>();

    final List<Transaction> transactions = this.transactionProvider
        .getTransactions(from, to, bankAccount);

    Double amountBasis = Double.valueOf(0.0);
    for (final Transaction transaction : transactions) {
      final Double amount = transaction.getAmount();
      if ((transaction.getCategory() != null) && (amount < 0)) {
        amountBasis += amount;
        final Category category = transaction.getCategory();

        if (!calculatedAbsolute.containsKey(category)) {
          calculatedAbsolute.put(category, amount);
        } else {
          calculatedAbsolute.replace(category, calculatedAbsolute.get(category) + amount);
        }
      }
    }

    if (amountBasis.equals(0.0)) {
      return calculatedPercentage;
    }

    for (final Entry<Category, Double> entry : calculatedAbsolute.entrySet()) {
      final Double percentage = entry.getValue() / amountBasis;
      calculatedPercentage.put(entry.getKey(), percentage);
    }

    return calculatedPercentage;
  }

  @Override
  public List<Category> getCategories(final ArrayList<String> categoryNames) {
    return this.categoryService.getCategories(categoryNames);
  }

  @Override
  public List<Category> getCategories() {
    return this.categoryService.getCategories();
  }

  @Override
  public List<String> getCategoryNames() {
    return this.categoryService.getCategoryNames();
  }

  @Override
  public Category persist(final Category category) {
    return this.categoryService.persist(category);
  }

  @Override
  public void deleteCategories(final List<Category> categories) {
    this.categoryService.deleteCategories(categories);
  }

  @Override
  public void deleteCategory(final String categoryName) {
    this.categoryService.delete(categoryName);
  }

}
