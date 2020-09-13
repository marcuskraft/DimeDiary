package com.dimediary.rest.controller;

import com.dimediary.openapi.api.CategoryApi;
import com.dimediary.openapi.model.Category;
import com.dimediary.port.in.CategoryProvider;
import com.dimediary.rest.controller.helper.ResponseFactory;
import com.dimediary.rest.converter.CategoryRestConverter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping("/api")
@Controller
public class CategoryController implements CategoryApi {

  private final CategoryProvider categoryProvider;
  private final CategoryRestConverter categoryRestConverter;
  private final ResponseFactory responseFactory;

  @Autowired
  public CategoryController(final CategoryProvider categoryProvider,
      final CategoryRestConverter categoryRestConverter,
      final ResponseFactory responseFactory) {
    this.categoryProvider = categoryProvider;
    this.categoryRestConverter = categoryRestConverter;
    this.responseFactory = responseFactory;
  }


  @Override
  public ResponseEntity<Category> createCategory(final Category category) {
    return this.responseFactory.created(
        this.categoryRestConverter
            .from(this.categoryProvider.persist(this.categoryRestConverter.to(category))));
  }

  @Override
  public ResponseEntity<Void> deleteCategory(final String categoryName) {
    this.categoryProvider.deleteCategory(categoryName);
    return this.responseFactory.okNoContent();
  }

  @Override
  public ResponseEntity<List<Category>> getCategories() {
    return this.responseFactory.ok(this.categoryProvider.getCategories().stream().map(
        this.categoryRestConverter::from).collect(
        Collectors.toList()));
  }

  @Override
  public ResponseEntity<Void> updateCategory(final String categoryName, final Category category) {
    if (!categoryName.equals(category.getName())) {
      return this.responseFactory.badRequest();
    }
    this.categoryProvider.persist(this.categoryRestConverter.to(category));
    return this.responseFactory.okNoContent();
  }
}
