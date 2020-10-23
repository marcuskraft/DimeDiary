package com.dimediary.persistence.repositories.cruds;

import com.dimediary.persistence.entities.RecurrenceExtraInstanceEntity;
import java.util.Collection;
import org.springframework.data.repository.CrudRepository;

public interface RecurrenceExtraInstanceCrudReporitory extends
    CrudRepository<RecurrenceExtraInstanceEntity, String> {

  Collection<RecurrenceExtraInstanceEntity> getAllByContinuousTransaction_Id(
      String continuousTransactionId);

}
