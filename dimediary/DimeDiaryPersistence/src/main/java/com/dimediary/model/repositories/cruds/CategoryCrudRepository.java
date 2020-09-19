package com.dimediary.model.repositories.cruds;

import com.dimediary.model.entities.CategoryEntity;
import org.springframework.data.repository.CrudRepository;

public interface CategoryCrudRepository extends CrudRepository<CategoryEntity, String> {

}
