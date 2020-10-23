package com.dimediary.persistence.converter;

import com.dimediary.persistence.entities.RecurrenceExceptionEntity;
import com.dimediary.persistence.entities.RecurrenceExtraInstanceEntity;
import java.time.LocalDate;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecurrenceExceptionsExtraTransformer {

  static LocalDate to(final RecurrenceExceptionEntity recurrenceExceptionEntity) {
    return recurrenceExceptionEntity.getExceptionDate();
  }

  static LocalDate to(final RecurrenceExtraInstanceEntity recurrenceExtraInstanceEntity) {
    return recurrenceExtraInstanceEntity.getInstanceDate();
  }

}
