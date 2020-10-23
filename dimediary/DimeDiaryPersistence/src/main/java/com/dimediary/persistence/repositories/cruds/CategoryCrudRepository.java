package com.dimediary.persistence.repositories.cruds;

import com.dimediary.persistence.entities.CategoryEntity;
import org.springframework.data.repository.CrudRepository;

public interface CategoryCrudRepository extends CrudRepository<CategoryEntity, String> {

}
