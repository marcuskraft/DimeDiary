package com.dimediary.model;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;

public class EntityManagerHelperImpl {

  public static final String PERSISTENCE_DERBY = "PersistenceDerby";
  public static final String PERSISTENCE_DERBY_TEST = "PersistenceDerbyTest";

  private static javax.persistence.EntityManager entityManager = null;
  private static javax.persistence.EntityManagerFactory emf = null;

  public static javax.persistence.EntityManager getEntityManager(final String persistenceUnit,
      final java.util.Map<String, String> properties) {
    if (com.dimediary.model.EntityManagerHelperImpl.entityManager == null) {
      if (com.dimediary.model.EntityManagerHelperImpl.emf == null) {
        if (properties == null) {
          com.dimediary.model.EntityManagerHelperImpl.emf = javax.persistence.Persistence
              .createEntityManagerFactory(persistenceUnit);
        } else {
          com.dimediary.model.EntityManagerHelperImpl.emf = javax.persistence.Persistence
              .createEntityManagerFactory(persistenceUnit, properties);
        }

      }
      com.dimediary.model.EntityManagerHelperImpl.entityManager = com.dimediary.model.EntityManagerHelperImpl.emf
          .createEntityManager();
    }
    return com.dimediary.model.EntityManagerHelperImpl.entityManager;
  }

  @Produces
  public static EntityManager getEntityManager() {
    return com.dimediary.model.EntityManagerHelperImpl.getEntityManager(
        com.dimediary.model.EntityManagerHelperImpl.PERSISTENCE_DERBY, null);
  }

  public static void closeEntityManager() {
    if (com.dimediary.model.EntityManagerHelperImpl.entityManager != null) {
      if (com.dimediary.model.EntityManagerHelperImpl.entityManager.getTransaction().isActive()) {
        com.dimediary.model.EntityManagerHelperImpl.entityManager.getTransaction().commit();
      }
      com.dimediary.model.EntityManagerHelperImpl.entityManager.close();
      com.dimediary.model.EntityManagerHelperImpl.entityManager = null;
    }
    if (com.dimediary.model.EntityManagerHelperImpl.emf != null) {
      com.dimediary.model.EntityManagerHelperImpl.emf.close();
      com.dimediary.model.EntityManagerHelperImpl.emf = null;
    }
  }

  public static boolean beginTransaction() {
    if (!com.dimediary.model.EntityManagerHelperImpl.getEntityManager().getTransaction()
        .isActive()) {
      com.dimediary.model.EntityManagerHelperImpl.getEntityManager().getTransaction().begin();
      return true;
    }
    return false;
  }

  public static boolean commitTransaction() {
    if (com.dimediary.model.EntityManagerHelperImpl.getEntityManager().getTransaction()
        .isActive()) {
      com.dimediary.model.EntityManagerHelperImpl.getEntityManager().getTransaction().commit();
      return true;
    }
    return false;
  }

  public static boolean rollbackTransaction() {
    if (com.dimediary.model.EntityManagerHelperImpl.getEntityManager().getTransaction()
        .isActive()) {
      com.dimediary.model.EntityManagerHelperImpl.getEntityManager().getTransaction().rollback();
      return true;
    }
    return false;
  }

}
