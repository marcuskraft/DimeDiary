package com.dimediary.persistence.converter;

import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UUIDTransformer {

  static String from(final UUID uuid) {
    if (uuid == null) {
      return null;
    }
    return uuid.toString();
  }

  static UUID to(final String uuid) {
    if (uuid == null) {
      return null;
    }
    return UUID.fromString(uuid);
  }

}
