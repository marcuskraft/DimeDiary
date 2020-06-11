package com.dimediary.model.converter;

@javax.persistence.Converter(autoApply = true)
public class LocalDateToDateConverter implements
    javax.persistence.AttributeConverter<java.time.LocalDate, java.util.Date> {

  @Override
  public java.util.Date convertToDatabaseColumn(final java.time.LocalDate date) {
    if (date == null) {
      return null;
    }
    return java.util.Date
        .from(date.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant());
  }

  @Override
  public java.time.LocalDate convertToEntityAttribute(final java.util.Date value) {
    if (value == null) {
      return null;
    }
    return java.time.Instant.ofEpochMilli(value.getTime()).atZone(java.time.ZoneId.systemDefault())
        .toLocalDate();
  }
}
