package com.dimediary.persistence.repositories.cruds;

import com.dimediary.persistence.entities.BankAccountEntity;
import org.springframework.data.repository.CrudRepository;

public interface BankAccountCrudRepository extends CrudRepository<BankAccountEntity, String> {

}
