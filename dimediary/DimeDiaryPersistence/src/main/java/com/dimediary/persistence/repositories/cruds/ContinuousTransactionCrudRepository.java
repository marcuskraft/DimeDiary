package com.dimediary.persistence.repositories.cruds;

import com.dimediary.persistence.entities.ContinuousTransactionEntity;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface ContinuousTransactionCrudRepository extends
    CrudRepository<ContinuousTransactionEntity, String> {

  List<ContinuousTransactionEntity> getAllByBankAccountId(String bankAccountId);

}
