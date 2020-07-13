package com.dimediary.model.repositories.cruds;

import com.dimediary.model.entities.BankAccountEntity;
import org.springframework.data.repository.CrudRepository;

public interface BankAccountCrudRepository extends CrudRepository<BankAccountEntity, String> {

}
