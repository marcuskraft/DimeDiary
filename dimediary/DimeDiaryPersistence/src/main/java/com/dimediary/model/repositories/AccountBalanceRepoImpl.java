package com.dimediary.model.repositories;

import com.dimediary.domain.Balance;
import com.dimediary.domain.BankAccount;
import com.dimediary.model.converter.BalanceHistoryTransformer;
import com.dimediary.port.out.AccountBalanceRepo;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
class AccountBalanceRepoImpl implements AccountBalanceRepo {


  @PersistenceContext
  private final EntityManager entityManager;

  private final BalanceHistoryTransformer balanceHistoryTransformer;

  @Autowired
  public AccountBalanceRepoImpl(final EntityManager entityManager,
      final BalanceHistoryTransformer balanceHistoryTransformer) {
    this.entityManager = entityManager;
    this.balanceHistoryTransformer = balanceHistoryTransformer;
  }

  @Override
  public java.util.List<Balance> getBalanceHistories(final BankAccount bankAccount) {
    Validate.notNull(bankAccount);

    this.log.info("getBalanceHistories for bank account: " + bankAccount.getName());

    final com.dimediary.model.entities.BankAccountEntity bankAccountEntity = this
        .findBankAccount(bankAccount);
    final java.util.List<com.dimediary.model.entities.BalanceHistoryEntity> balanceHistories = this.entityManager
        .createNamedQuery("accountBalance", com.dimediary.model.entities.BalanceHistoryEntity.class)
        .setParameter("bankAccount", bankAccountEntity).getResultList();
    return this.entitiesToDomains(balanceHistories);

  }

  @Override
  public java.util.List<Balance> getBalanceHistoriesAfterDate(final BankAccount bankAccount,
      final java.time.LocalDate date) {
    Validate.notNull(bankAccount);
    Validate.notNull(date);
    this.log.info("getBalanceHistoriesAfterDate for bank account: " + bankAccount.getName()
        + " and after date: "
        + date.toString());

    final com.dimediary.model.entities.BankAccountEntity bankAccountEntity = this
        .findBankAccount(bankAccount);

    final java.util.List<com.dimediary.model.entities.BalanceHistoryEntity> balanceHistories = this.entityManager
        .createNamedQuery(com.dimediary.model.entities.BalanceHistoryEntity.ACCOUNT_BALANCE_DATE,
            com.dimediary.model.entities.BalanceHistoryEntity.class)
        .setParameter("date", date).setParameter("bankAccount", bankAccountEntity).getResultList();
    return this.entitiesToDomains(balanceHistories);
  }

  @Override
  public Balance getBalanceHistory(final BankAccount bankAccount,
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

    final com.dimediary.model.entities.BalanceHistoryEntity balanceEntity = this.entityManager
        .find(com.dimediary.model.entities.BalanceHistoryEntity.class, balanceHistoryPK);

    return this.entityToDomain(balanceEntity);
  }

  @Override
  public Balance getBalanceHistoryBefore(final BankAccount bankAccount,
      final java.time.LocalDate date) {
    Validate.notNull(bankAccount);
    Validate.notNull(date);

    this.log
        .info("getLastBalanceHistory for bank account: {} and before: {}", bankAccount.getName(),
            date);

    try {
      final com.dimediary.model.entities.BankAccountEntity bankAccountEntity = this
          .findBankAccount(bankAccount);

      return this.entityToDomain(this.entityManager
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
  public Balance getLastBalanceHistory(final BankAccount bankAccount) {
    Validate.notNull(bankAccount);

    this.log.info("getLastBalanceHistory for bank account: " + bankAccount.getName());

    try {
      final com.dimediary.model.entities.BankAccountEntity bankAccountEntity = this
          .findBankAccount(bankAccount);

      return this.entityToDomain(this.entityManager
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
  public void persist(final Balance balance) {
    Validate.notNull(balance);
    this.log.info("persist BalanceHistory for bank account: " + balance.getBankAccount()
        + " and date: "
        + balance.getDate());
    try {
      final com.dimediary.model.entities.BalanceHistoryEntity balanceHistoryEntity = this.balanceHistoryTransformer
          .balanceHistoryToBalanceHistoryEntity(balance,
              this.findBankAccount(balance.getBankAccount()));

      if (this.findBalanceHistoryEntity(balance) == null) {
        this.entityManager.persist(balanceHistoryEntity);
      } else {
        this.entityManager.merge(balanceHistoryEntity);
      }

    } catch (final Exception e) {
      this.log.error("can't persist balance history", e);
      throw e;
    }
  }

  @Override
  public void persistBalanceHistories(final java.util.List<Balance> balanceHistories) {
    Validate.notNull(balanceHistories);
    if (balanceHistories.isEmpty()) {
      return;
    }

    for (final Balance balance : balanceHistories) {
      this.persist(balance);
    }

  }

  @Override
  public void delete(final Balance balance) {
    Validate.notNull(balance);

    try {
      if (balance.getBankAccount() != null) {
        this.log.info("delete BalanceHistory for bank account: " + balance.getBankAccount()
            .getBankName()
            + " and date: " + balance.getDate());
      }

      final com.dimediary.model.entities.BalanceHistoryEntity balanceHistoryEntity = this
          .findBalanceHistoryEntity(balance);

      if (balanceHistoryEntity != null) {
        this.entityManager.remove(balanceHistoryEntity);
      }


    } catch (final Exception e) {
      this.log.error("can't delete BalanceHistory for bank account: {} and date: {}",
          balance.getBankAccount().getBankName(), balance.getDate(), e);
      throw e;
    }
  }

  /**
   * @param balanceHistories list of balance histories to delete
   */
  @Override
  public void deleteBalanceHistories(final java.util.List<Balance> balanceHistories) {
    Validate.notNull(balanceHistories);
    if (balanceHistories.isEmpty()) {
      return;
    }

    for (final Balance balance : balanceHistories) {
      this.delete(balance);
    }

  }

  /**
   * @param bankAccount bank account for which all balance histories will be deleted
   */
  @Override
  public void deleteBalanceHistories(final BankAccount bankAccount) {
    Validate.notNull(bankAccount);

    final java.util.List<Balance> balanceHistories = this.getBalanceHistories(bankAccount);
    if (!balanceHistories.isEmpty()) {
      this.deleteBalanceHistories(balanceHistories);
    }

  }

  private java.util.List<Balance> entitiesToDomains(
      final java.util.List<com.dimediary.model.entities.BalanceHistoryEntity> balanceHistories) {
    return balanceHistories.stream().map((balance) -> this.entityToDomain(balance)).collect(
        java.util.stream.Collectors.toList());
  }

  private Balance entityToDomain(
      final com.dimediary.model.entities.BalanceHistoryEntity balanceEntity) {
    return this.balanceHistoryTransformer.balanceHistoryEntityToBalanceHistory(balanceEntity);
  }

  private com.dimediary.model.entities.BankAccountEntity findBankAccount(
      final BankAccount bankAccount) {
    if (bankAccount != null && bankAccount.getName() != null && !bankAccount.getName().isEmpty()) {
      final com.dimediary.model.entities.BankAccountEntity bankAccountEntity = this.entityManager
          .find(com.dimediary.model.entities.BankAccountEntity.class, bankAccount.getName());
      this.entityManager.merge(bankAccountEntity);
      return bankAccountEntity;

    } else {
      return null;
    }

  }

  private com.dimediary.model.entities.BalanceHistoryEntity findBalanceHistoryEntity(
      final Balance balance) {
    if (balance != null && balance.getBankAccount() != null
        && balance.getDate() != null) {
      final com.dimediary.model.entities.BankAccountEntity bankAccountEntity = this
          .findBankAccount(balance.getBankAccount());
      if (bankAccountEntity != null) {

        try {
          return this.entityManager
              .createNamedQuery(
                  com.dimediary.model.entities.BalanceHistoryEntity.ACCOUNT_BALANCE_EXACT_DATE,
                  com.dimediary.model.entities.BalanceHistoryEntity.class)
              .setParameter("bankAccount", bankAccountEntity)
              .setParameter("date", balance.getDate()).getSingleResult();
        } catch (final javax.persistence.NoResultException e) {
          this.log.info("No balance history found for bank account{} and date {}",
              balance.getBankAccount().getName(), balance.getDate().toString());
        }
      }
    }
    return null;
  }

}
