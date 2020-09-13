package com.dimediary.rest.converter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocalDateTimeOffSetDateTimeConverter {


  public static OffsetDateTime from(final LocalDateTime localDateTime) {
    return localDateTime.atOffset(ZoneId.of("Europe/Berlin").getRules().getOffset(localDateTime));
  }

  public static LocalDateTime to(final OffsetDateTime offsetDateTime) {
    return offsetDateTime.toLocalDateTime();
  }

}
