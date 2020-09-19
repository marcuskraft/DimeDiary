package com.dimediary.model.repositories.cruds;

import com.dimediary.model.entities.BankAccountCategoryEntity;
import org.springframework.data.repository.CrudRepository;

public interface BankAccountCategoryCrudRepository extends
    CrudRepository<BankAccountCategoryEntity, String> {

}
