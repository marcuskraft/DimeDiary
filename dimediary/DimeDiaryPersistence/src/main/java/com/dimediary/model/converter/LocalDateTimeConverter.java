package com.dimediary.model.converter;

@javax.persistence.Converter(autoApply = true)
public class LocalDateTimeConverter implements
    javax.persistence.AttributeConverter<java.time.LocalDateTime, java.util.Date> {

  @Override
  public java.util.Date convertToDatabaseColumn(final java.time.LocalDateTime attribute) {
    if (attribute == null) {
      return null;
    }
    return java.util.Date.from(attribute.atZone(java.time.ZoneId.systemDefault()).toInstant());
  }

  @Override
  public java.time.LocalDateTime convertToEntityAttribute(final java.util.Date dbData) {
    if (dbData == null) {
      return null;
    }
    return java.time.LocalDateTime.ofInstant(dbData.toInstant(), java.time.ZoneId.systemDefault());
  }

}
