package com.dimediary.model.converter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateToDateConverter implements
    AttributeConverter<LocalDate, Date> {

  @Override
  public Date convertToDatabaseColumn(final LocalDate date) {
    if (date == null) {
      return null;
    }
    return Date
        .from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
  }

  @Override
  public LocalDate convertToEntityAttribute(final Date value) {
    if (value == null) {
      return null;
    }
    return Instant.ofEpochMilli(value.getTime()).atZone(ZoneId.systemDefault())
        .toLocalDate();
  }
}
