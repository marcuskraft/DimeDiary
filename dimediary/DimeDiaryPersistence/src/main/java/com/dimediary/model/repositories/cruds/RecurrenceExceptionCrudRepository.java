package com.dimediary.model.repositories.cruds;

import com.dimediary.model.entities.RecurrenceExceptionEntity;
import java.util.Collection;
import org.springframework.data.repository.CrudRepository;

public interface RecurrenceExceptionCrudRepository extends
    CrudRepository<RecurrenceExceptionEntity, String> {


  Collection<RecurrenceExceptionEntity> getAllByContinuousTransaction_Id(
      String continuousTransactionId);

}
