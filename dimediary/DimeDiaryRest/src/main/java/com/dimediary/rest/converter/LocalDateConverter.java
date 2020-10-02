package com.dimediary.rest.converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocalDateConverter {

  static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  public static String localDateToIsoString(final LocalDate localDate) {
    return localDate.format(dateTimeFormatter);
  }

  public static LocalDate isoStringToLocalDate(final String isoString) {
    return LocalDate.parse(isoString, dateTimeFormatter);
  }

}
