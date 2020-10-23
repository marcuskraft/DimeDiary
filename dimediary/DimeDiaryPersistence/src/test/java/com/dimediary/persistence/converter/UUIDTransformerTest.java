package com.dimediary.persistence.converter;

import java.util.UUID;
import org.junit.jupiter.api.Test;

public class UUIDTransformerTest {

  @Test
  void UUIDtoStringAndBack() {

    final String uuidString = "9eae39b6-84c9-4819-ad8f-d0198f159182";

    final UUID uuid = UUID.fromString(uuidString);

    final String uuidStringAfter = uuid.toString();

    assert uuidString.equals(uuidStringAfter);

  }
}
