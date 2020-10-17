package com.dimediary.rest.converter;

import com.dimediary.domain.RecurrenceSettings;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {LocalDateConverter.class,
    RecurrenceTypeRestConverter.class, DayOfWeekRestConverter.class})
public interface RecurrenceSettingsRestConverter {

  RecurrenceSettings to(com.dimediary.openapi.model.RecurrenceSettings recurrenceSettings);

  com.dimediary.openapi.model.RecurrenceSettings from(RecurrenceSettings recurrenceSettings);

}
