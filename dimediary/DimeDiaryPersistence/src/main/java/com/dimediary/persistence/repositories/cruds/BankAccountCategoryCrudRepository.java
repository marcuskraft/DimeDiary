package com.dimediary.persistence.repositories.cruds;

import com.dimediary.persistence.entities.BankAccountCategoryEntity;
import org.springframework.data.repository.CrudRepository;

public interface BankAccountCategoryCrudRepository extends
    CrudRepository<BankAccountCategoryEntity, String> {

}
