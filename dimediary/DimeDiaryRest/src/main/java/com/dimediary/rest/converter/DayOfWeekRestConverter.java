package com.dimediary.rest.converter;

import java.time.DayOfWeek;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DayOfWeekRestConverter {

  static DayOfWeek to(final com.dimediary.openapi.model.DayOfWeekAPI dayOfWeekAPI) {
    switch (dayOfWeekAPI) {
      case MONDAY:
        return DayOfWeek.MONDAY;
      case TUESDAY:
        return DayOfWeek.TUESDAY;
      case WEDNESDAY:
        return DayOfWeek.WEDNESDAY;
      case THURSDAY:
        return DayOfWeek.THURSDAY;
      case FRIDAY:
        return DayOfWeek.FRIDAY;
      case SATURDAY:
        return DayOfWeek.SATURDAY;
      case SUNDAY:
      default:
        return DayOfWeek.SUNDAY;
    }
  }

  static com.dimediary.openapi.model.DayOfWeekAPI from(final DayOfWeek dayOfWeek) {
    switch (dayOfWeek) {
      case MONDAY:
        return com.dimediary.openapi.model.DayOfWeekAPI.MONDAY;
      case TUESDAY:
        return com.dimediary.openapi.model.DayOfWeekAPI.TUESDAY;
      case WEDNESDAY:
        return com.dimediary.openapi.model.DayOfWeekAPI.WEDNESDAY;
      case THURSDAY:
        return com.dimediary.openapi.model.DayOfWeekAPI.THURSDAY;
      case FRIDAY:
        return com.dimediary.openapi.model.DayOfWeekAPI.FRIDAY;
      case SATURDAY:
        return com.dimediary.openapi.model.DayOfWeekAPI.SATURDAY;
      case SUNDAY:
      default:
        return com.dimediary.openapi.model.DayOfWeekAPI.SUNDAY;
    }
  }


}
