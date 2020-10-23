package com.dimediary.persistence.converter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateTimeConverter implements
    AttributeConverter<LocalDateTime, Date> {

  @Override
  public Date convertToDatabaseColumn(final LocalDateTime attribute) {
    if (attribute == null) {
      return null;
    }
    return Date.from(attribute.atZone(ZoneId.systemDefault()).toInstant());
  }

  @Override
  public LocalDateTime convertToEntityAttribute(final Date dbData) {
    if (dbData == null) {
      return null;
    }
    return LocalDateTime.ofInstant(dbData.toInstant(), ZoneId.systemDefault());
  }

}
