package com.dimediary.model.repositories;

import com.dimediary.domain.Balance;
import com.dimediary.domain.BankAccount;
import com.dimediary.model.converter.BalanceHistoryTransformer;
import com.dimediary.model.entities.BalanceEntity;
import com.dimediary.model.entities.BankAccountEntity;
import com.dimediary.port.out.AccountBalanceRepo;
import java.time.LocalDate;
import java.util.List;
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
  public List<Balance> getBalanceHistoriesAfterDate(final BankAccount bankAccount,
      final LocalDate date) {
    Validate.notNull(bankAccount);
    Validate.notNull(date);
    AccountBalanceRepoImpl.log
        .info("getBalanceHistoriesAfterDate for bank account: " + bankAccount.getName()
            + " and after date: "
            + date.toString());

    final BankAccountEntity bankAccountEntity = this
        .findBankAccount(bankAccount);

    final List<BalanceEntity> balanceHistories = this.entityManager
        .createNamedQuery(BalanceEntity.ACCOUNT_BALANCE_DATE, BalanceEntity.class)
        .setParameter("date", date).setParameter("bankAccount", bankAccountEntity).getResultList();

    return this.entitiesToDomains(balanceHistories);
  }

  @Override
  public Balance getBalanceHistoryBefore(final BankAccount bankAccount,
      final java.time.LocalDate date) {
    Validate.notNull(bankAccount);
    Validate.notNull(date);

    AccountBalanceRepoImpl.log
        .info("getLastBalanceHistory for bank account: {} and before: {}", bankAccount.getName(),
            date);

    try {
      final com.dimediary.model.entities.BankAccountEntity bankAccountEntity = this
          .findBankAccount(bankAccount);

      return this.entityToDomain(this.entityManager
          .createNamedQuery(
              BalanceEntity.LAST_ACCOUNT_BALANCE_BEFORE,
              BalanceEntity.class)
          .setParameter("bankAccount", bankAccountEntity).setParameter("date", date)
          .setFirstResult(0)
          .setMaxResults(1).getSingleResult());
    } catch (final javax.persistence.NoResultException e) {
      AccountBalanceRepoImpl.log
          .warn("no balance history in database for the bank account {} before {} ",
              bankAccount.getName(),
              date, e);
      return null;
    }
  }

  @Override
  public Balance getLastBalanceHistory(final BankAccount bankAccount) {
    Validate.notNull(bankAccount);

    AccountBalanceRepoImpl.log
        .info("getLastBalanceHistory for bank account: " + bankAccount.getName());

    try {
      final com.dimediary.model.entities.BankAccountEntity bankAccountEntity = this
          .findBankAccount(bankAccount);

      return this.entityToDomain(this.entityManager
          .createNamedQuery("lastAccountBalance",
              BalanceEntity.class)
          .setParameter("bankAccount", bankAccountEntity).getSingleResult());
    } catch (final javax.persistence.NoResultException e) {
      AccountBalanceRepoImpl.log
          .warn("no balance history in database for the bank account: " + bankAccount.getName(), e);
      return null;
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
      final java.util.List<BalanceEntity> balanceHistories) {
    return balanceHistories.stream().map(this::entityToDomain).collect(
        java.util.stream.Collectors.toList());
  }

  private Balance entityToDomain(
      final BalanceEntity balanceEntity) {
    return this.balanceHistoryTransformer.balanceHistoryEntityToBalanceHistory(balanceEntity);
  }

  private BankAccountEntity findBankAccount(
      final BankAccount bankAccount) {
    if (bankAccount != null && bankAccount.getName() != null && !bankAccount.getName().isEmpty()) {
      final BankAccountEntity bankAccountEntity = this.entityManager
          .find(BankAccountEntity.class, bankAccount.getName());
      this.entityManager.merge(bankAccountEntity);
      return bankAccountEntity;

    } else {
      return null;
    }

  }

  private BalanceEntity findBalanceHistoryEntity(
      final Balance balance) {
    if (balance != null && balance.getBankAccount() != null
        && balance.getDate() != null) {
      final BankAccountEntity bankAccountEntity = this
          .findBankAccount(balance.getBankAccount());
      if (bankAccountEntity != null) {

        try {
          return this.entityManager
              .createNamedQuery(
                  BalanceEntity.ACCOUNT_BALANCE_EXACT_DATE,
                  BalanceEntity.class)
              .setParameter("bankAccount", bankAccountEntity)
              .setParameter("date", balance.getDate()).getSingleResult();
        } catch (final javax.persistence.NoResultException e) {
          AccountBalanceRepoImpl.log.info("No balance history found for bank account{} and date {}",
              balance.getBankAccount().getName(), balance.getDate().toString());
        }
      }
    }
    return null;
  }

  private void delete(final Balance balance) {
    Validate.notNull(balance);

    try {
      if (balance.getBankAccount() != null) {
        AccountBalanceRepoImpl.log
            .info("delete BalanceHistory for bank account: " + balance.getBankAccount()
                .getBankName()
                + " and date: " + balance.getDate());
      }

      final BalanceEntity balanceEntity = this
          .findBalanceHistoryEntity(balance);

      if (balanceEntity != null) {
        this.entityManager.remove(balanceEntity);
      }


    } catch (final Exception e) {
      AccountBalanceRepoImpl.log
          .error("can't delete BalanceHistory for bank account: {} and date: {}",
              balance.getBankAccount().getBankName(), balance.getDate(), e);
      throw e;
    }
  }


  private void deleteBalanceHistories(final List<Balance> balanceHistories) {
    Validate.notNull(balanceHistories);
    if (balanceHistories.isEmpty()) {
      return;
    }

    for (final Balance balance : balanceHistories) {
      this.delete(balance);
    }

  }

  private void persist(final Balance balance) {
    Validate.notNull(balance);
    AccountBalanceRepoImpl.log
        .info("persist BalanceHistory for bank account: " + balance.getBankAccount()
            + " and date: "
            + balance.getDate());
    try {
      final BalanceEntity balanceEntity = this.balanceHistoryTransformer
          .balanceHistoryToBalanceHistoryEntity(balance,
              this.findBankAccount(balance.getBankAccount()));

      if (this.findBalanceHistoryEntity(balance) == null) {
        this.entityManager.persist(balanceEntity);
      } else {
        this.entityManager.merge(balanceEntity);
      }

    } catch (final Exception e) {
      AccountBalanceRepoImpl.log.error("can't persist balance history", e);
      throw e;
    }
  }

  private java.util.List<Balance> getBalanceHistories(final BankAccount bankAccount) {
    Validate.notNull(bankAccount);

    AccountBalanceRepoImpl.log
        .info("getBalanceHistories for bank account: " + bankAccount.getName());

    final com.dimediary.model.entities.BankAccountEntity bankAccountEntity = this
        .findBankAccount(bankAccount);
    final java.util.List<BalanceEntity> balanceHistories = this.entityManager
        .createNamedQuery("accountBalance", BalanceEntity.class)
        .setParameter("bankAccount", bankAccountEntity).getResultList();
    return this.entitiesToDomains(balanceHistories);

  }

}
