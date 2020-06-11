package com.dimediary.model.repositories;

import com.dimediary.domain.BalanceHistory;
import com.dimediary.domain.BankAccount;
import com.dimediary.model.converter.BalanceHistoryTransformer;
import com.dimediary.model.repositories.helper.DatabaseTransactionProviderImpl;
import com.dimediary.port.out.AccountBalanceRepo;
import javax.inject.Inject;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.Logger;

class AccountBalanceRepoImpl implements AccountBalanceRepo {

  @Inject
  private Logger log;

  @Inject
  private DatabaseTransactionProviderImpl entityManagerService;

  @Inject
  private BalanceHistoryTransformer balanceHistoryTransformer;

  @Override
  public java.util.List<BalanceHistory> getBalanceHistories(final BankAccount bankAccount) {
    Validate.notNull(bankAccount);

    this.log.info("getBalanceHistories for bank account: " + bankAccount.getName());

    final com.dimediary.model.entities.BankAccountEntity bankAccountEntity = this
        .findBankAccount(bankAccount);
    final java.util.List<com.dimediary.model.entities.BalanceHistoryEntity> balanceHistories = this.entityManagerService
        .getEntityManager()
        .createNamedQuery("accountBalance", com.dimediary.model.entities.BalanceHistoryEntity.class)
        .setParameter("bankAccount", bankAccountEntity).getResultList();
    return this.entitiesToDomains(balanceHistories);

  }

  @Override
  public java.util.List<BalanceHistory> getBalanceHistoriesAfterDate(final BankAccount bankAccount,
      final java.time.LocalDate date) {
    Validate.notNull(bankAccount);
    Validate.notNull(date);
    this.log.info("getBalanceHistoriesAfterDate for bank account: " + bankAccount.getName()
        + " and after date: "
        + date.toString());

    final com.dimediary.model.entities.BankAccountEntity bankAccountEntity = this
        .findBankAccount(bankAccount);

    final java.util.List<com.dimediary.model.entities.BalanceHistoryEntity> balanceHistories = this.entityManagerService
        .getEntityManager()
        .createNamedQuery(com.dimediary.model.entities.BalanceHistoryEntity.ACCOUNT_BALANCE_DATE,
            com.dimediary.model.entities.BalanceHistoryEntity.class)
        .setParameter("date", date).setParameter("bankAccount", bankAccountEntity).getResultList();
    return this.entitiesToDomains(balanceHistories);
  }

  @Override
  public BalanceHistory getBalanceHistory(final BankAccount bankAccount,
      final java.time.LocalDate date) {
    Validate.notNull(bankAccount);
    Validate.notNull(date);
    this.log.info(
        "getBalanceHistory for bank account: " + bankAccount.getName() + " and on date: " + date
            .toString());

    final com.dimediary.model.entities.BankAccountEntity bankAccountEntity = this
        .findBankAccount(bankAccount);
    final com.dimediary.model.entities.BalanceHistoryPK balanceHistoryPK = new com.dimediary.model.entities.BalanceHistoryPK(
        bankAccountEntity, date);

    final com.dimediary.model.entities.BalanceHistoryEntity balanceEntity = this.entityManagerService
        .getEntityManager()
        .find(com.dimediary.model.entities.BalanceHistoryEntity.class, balanceHistoryPK);

    return this.entityToDomain(balanceEntity);
  }

  @Override
  public BalanceHistory getBalanceHistoryBefore(final BankAccount bankAccount,
      final java.time.LocalDate date) {
    Validate.notNull(bankAccount);
    Validate.notNull(date);

    this.log
        .info("getLastBalanceHistory for bank account: {} and before: {}", bankAccount.getName(),
            date);

    try {
      final com.dimediary.model.entities.BankAccountEntity bankAccountEntity = this
          .findBankAccount(bankAccount);

      return this.entityToDomain(this.entityManagerService.getEntityManager()
          .createNamedQuery(
              com.dimediary.model.entities.BalanceHistoryEntity.LAST_ACCOUNT_BALANCE_BEFORE,
              com.dimediary.model.entities.BalanceHistoryEntity.class)
          .setParameter("bankAccount", bankAccountEntity).setParameter("date", date)
          .setFirstResult(0)
          .setMaxResults(1).getSingleResult());
    } catch (final javax.persistence.NoResultException e) {
      this.log.warn("no balance history in database for the bank account {} before {} ",
          bankAccount.getName(),
          date, e);
      return null;
    }
  }

  @Override
  public BalanceHistory getLastBalanceHistory(final BankAccount bankAccount) {
    Validate.notNull(bankAccount);

    this.log.info("getLastBalanceHistory for bank account: " + bankAccount.getName());

    try {
      final com.dimediary.model.entities.BankAccountEntity bankAccountEntity = this
          .findBankAccount(bankAccount);

      return this.entityToDomain(this.entityManagerService.getEntityManager()
          .createNamedQuery("lastAccountBalance",
              com.dimediary.model.entities.BalanceHistoryEntity.class)
          .setParameter("bankAccount", bankAccountEntity).getSingleResult());
    } catch (final javax.persistence.NoResultException e) {
      this.log
          .warn("no balance history in database for the bank account: " + bankAccount.getName(), e);
      return null;
    }
  }

  @Override
  public void persist(final BalanceHistory balanceHistory) {
    Validate.notNull(balanceHistory);
    this.log.info("persist BalanceHistory for bank account: " + balanceHistory.getBankAccount()
        + " and date: "
        + balanceHistory.getDate());
    final boolean ownTransaction = this.entityManagerService.beginTransaction();

    try {
      final com.dimediary.model.entities.BalanceHistoryEntity balanceHistoryEntity = this.balanceHistoryTransformer
          .balanceHistoryToBalanceHistoryEntity(balanceHistory,
              this.findBankAccount(balanceHistory.getBankAccount()));

      if (this.findBalanceHistoryEntity(balanceHistory) == null) {
        this.entityManagerService.getEntityManager().persist(balanceHistoryEntity);
      } else {
        this.entityManagerService.getEntityManager().merge(balanceHistoryEntity);
      }

    } catch (final Exception e) {
      this.log.error("can't persist balance history", e);
      this.entityManagerService.rollbackTransaction();
      throw e;
    }

    if (ownTransaction) {
      this.entityManagerService.commitTransaction();
    }
  }

  @Override
  public void persistBalanceHistories(final java.util.List<BalanceHistory> balanceHistories) {
    Validate.notNull(balanceHistories);
    if (balanceHistories.isEmpty()) {
      return;
    }
    final boolean ownTransaction = this.entityManagerService.beginTransaction();

    for (final BalanceHistory balanceHistory : balanceHistories) {
      this.persist(balanceHistory);
    }

    if (ownTransaction) {
      this.entityManagerService.commitTransaction();
    }
  }

  @Override
  public void delete(final BalanceHistory balanceHistory) {
    Validate.notNull(balanceHistory);

    try {
      if (balanceHistory.getBankAccount() != null) {
        this.log.info("delete BalanceHistory for bank account: " + balanceHistory.getBankAccount()
            .getBankName()
            + " and date: " + balanceHistory.getDate());
      }

      final boolean ownTransaction = this.entityManagerService.beginTransaction();

      final com.dimediary.model.entities.BalanceHistoryEntity balanceHistoryEntity = this
          .findBalanceHistoryEntity(balanceHistory);

      if (balanceHistoryEntity != null) {
        this.entityManagerService.getEntityManager().remove(balanceHistoryEntity);
      }

      if (ownTransaction) {
        this.entityManagerService.commitTransaction();
      }
    } catch (final Exception e) {
      this.log.error(new org.apache.logging.log4j.message.ParameterizedMessage(
          "can't delete BalanceHistory for bank account: {} and date: {}",
          balanceHistory.getBankAccount().getBankName(), balanceHistory.getDate()), e);
      this.entityManagerService.rollbackTransaction();
      throw e;
    }
  }

  /**
   * @param balanceHistories list of balance histories to delete
   */
  @Override
  public void deleteBalanceHistories(final java.util.List<BalanceHistory> balanceHistories) {
    Validate.notNull(balanceHistories);
    if (balanceHistories.isEmpty()) {
      return;
    }
    final boolean ownTransaction = this.entityManagerService.beginTransaction();

    for (final BalanceHistory balanceHistory : balanceHistories) {
      this.delete(balanceHistory);
    }

    if (ownTransaction) {
      this.entityManagerService.commitTransaction();
    }
  }

  /**
   * @param bankAccount bank account for which all balance histories will be deleted
   */
  @Override
  public void deleteBalanceHistories(final BankAccount bankAccount) {
    Validate.notNull(bankAccount);
    final boolean ownTransaction = this.entityManagerService.beginTransaction();

    final java.util.List<BalanceHistory> balanceHistories = this.getBalanceHistories(bankAccount);
    if (!balanceHistories.isEmpty()) {
      this.deleteBalanceHistories(balanceHistories);
    }

    if (ownTransaction) {
      this.entityManagerService.commitTransaction();
    }
  }

  private java.util.List<BalanceHistory> entitiesToDomains(
      final java.util.List<com.dimediary.model.entities.BalanceHistoryEntity> balanceHistories) {
    return balanceHistories.stream().map((balance) -> this.entityToDomain(balance)).collect(
        java.util.stream.Collectors.toList());
  }

  private BalanceHistory entityToDomain(
      final com.dimediary.model.entities.BalanceHistoryEntity balanceEntity) {
    return this.balanceHistoryTransformer.balanceHistoryEntityToBalanceHistory(balanceEntity);
  }

  private com.dimediary.model.entities.BankAccountEntity findBankAccount(
      final BankAccount bankAccount) {
    if (bankAccount != null && bankAccount.getName() != null && !bankAccount.getName().isEmpty()) {
      final com.dimediary.model.entities.BankAccountEntity bankAccountEntity = this.entityManagerService
          .getEntityManager()
          .find(com.dimediary.model.entities.BankAccountEntity.class, bankAccount.getName());
      this.entityManagerService.getEntityManager().merge(bankAccountEntity);
      return bankAccountEntity;

    } else {
      return null;
    }

  }

  private com.dimediary.model.entities.BalanceHistoryEntity findBalanceHistoryEntity(
      final BalanceHistory balanceHistory) {
    if (balanceHistory != null && balanceHistory.getBankAccount() != null
        && balanceHistory.getDate() != null) {
      final com.dimediary.model.entities.BankAccountEntity bankAccountEntity = this
          .findBankAccount(balanceHistory.getBankAccount());
      if (bankAccountEntity != null) {

        try {
          return this.entityManagerService.getEntityManager()
              .createNamedQuery(
                  com.dimediary.model.entities.BalanceHistoryEntity.ACCOUNT_BALANCE_EXACT_DATE,
                  com.dimediary.model.entities.BalanceHistoryEntity.class)
              .setParameter("bankAccount", bankAccountEntity)
              .setParameter("date", balanceHistory.getDate()).getSingleResult();
        } catch (final javax.persistence.NoResultException e) {
          this.log.info("No balance history found for bank account{} and date {}",
              balanceHistory.getBankAccount().getName(), balanceHistory.getDate().toString());
        }
      }
    }
    return null;
  }

}
