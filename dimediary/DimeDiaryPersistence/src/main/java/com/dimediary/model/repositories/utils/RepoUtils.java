package com.dimediary.model.repositories.utils;

import java.util.UUID;
import javax.persistence.NoResultException;

public class RepoUtils {

  public static NoResultException getNoResultException(final String type, final UUID id) {
    return new NoResultException("no " + type + " with id " + id + " found");
  }

}
