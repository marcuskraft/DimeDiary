package com.dimediary.model.converter;

import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UUIDTransformer {

  static String from(final UUID uuid) {
    return uuid.toString();
  }

  static UUID to(final String uuid) {
    return UUID.fromString(uuid);
  }

}
