package com.dimediary.rest.converter;

import com.dimediary.domain.RecurrenceSettings;
import com.dimediary.openapi.model.RecurrenceType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecurrenceTypeRestConverter {

  static RecurrenceSettings.RecurrenceType to(final RecurrenceType recurrenceType) {
    switch (recurrenceType) {
      case DAILY:
        return RecurrenceSettings.RecurrenceType.DAILY;
      case WEEKLY:
        return RecurrenceSettings.RecurrenceType.WEEKLY;
      case YEARLY:
        return RecurrenceSettings.RecurrenceType.YEARLY;
      case MONTHLY:
      default:
        return RecurrenceSettings.RecurrenceType.MONTHLY;
    }
  }

  static RecurrenceType from(final RecurrenceSettings.RecurrenceType recurrenceType) {
    switch (recurrenceType) {
      case DAILY:
        return RecurrenceType.DAILY;
      case WEEKLY:
        return RecurrenceType.WEEKLY;
      case YEARLY:
        return RecurrenceType.YEARLY;
      case MONTHLY:
      default:
        return RecurrenceType.MONTHLY;
    }
  }

}
