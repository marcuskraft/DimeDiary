package com.dimediary.model.repositories.helper;

import com.dimediary.domain.helper.DatabaseTransactionProvider;
import javax.inject.Inject;
import javax.persistence.EntityManager;

public class DatabaseTransactionProviderImpl implements DatabaseTransactionProvider {

  @Inject
  protected EntityManager entityManager;

  @Override
  public boolean beginTransaction() {
    if (!this.entityManager.getTransaction().isActive()) {
      this.entityManager.getTransaction().begin();
      return true;
    }
    return false;
  }

  @Override
  public boolean commitTransaction() {
    final javax.persistence.EntityTransaction transaction = this.entityManager.getTransaction();
    final boolean active = transaction.isActive();
    if (active) {
      transaction.commit();
      return true;
    }
    return false;
  }

  @Override
  public boolean rollbackTransaction() {
    if (this.entityManager.getTransaction().isActive()) {
      this.entityManager.getTransaction().rollback();
      return true;
    }
    return false;
  }

  @Override
  public void close() {
    com.dimediary.model.EntityManagerHelperImpl.closeEntityManager();
  }

  public javax.persistence.EntityManager getEntityManager() {
    return this.entityManager;
  }

}
