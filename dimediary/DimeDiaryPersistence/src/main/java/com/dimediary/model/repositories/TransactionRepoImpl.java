package com.dimediary.model.repositories;

import com.dimediary.domain.BankAccount;
import com.dimediary.domain.ContinuousTransaction;
import com.dimediary.domain.Transaction;
import com.dimediary.model.converter.TransactionTransformer;
import com.dimediary.model.entities.TransactionEntity;
import com.dimediary.port.out.TransactionRepo;
import java.util.UUID;
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
public class TransactionRepoImpl implements TransactionRepo {

  @PersistenceContext
  private final EntityManager entityManager;


  private final TransactionTransformer transactionTransformer;

  @Autowired
  public TransactionRepoImpl(final EntityManager entityManager,
      final TransactionTransformer transactionTransformer) {
    this.entityManager = entityManager;
    this.transactionTransformer = transactionTransformer;
  }


  @Override
  public Transaction getTransaction(final UUID transactionId) {
    final TransactionEntity transactionEntity = this.entityManager
        .find(TransactionEntity.class, transactionId);
    return this.transactionTransformer.transactionEntityToTransaction(transactionEntity);
  }

  @Override
  public java.util.List<Transaction> getTransactions(final java.time.LocalDate dateFrom,
      final java.time.LocalDate dateUntil,
      final BankAccount bankAccount) {
    Validate.notNull(dateFrom);
    Validate.notNull(dateUntil);
    Validate.notNull(bankAccount);

    com.dimediary.model.repositories.TransactionRepoImpl.log
        .info("getTransactions from date: " + dateFrom.toString() + " until date: "
            + dateUntil.toString() + " for bank account: " + bankAccount.getName());
    final java.util.List<com.dimediary.model.entities.TransactionEntity> transactions;

    final com.dimediary.model.entities.BankAccountEntity bankAccountEntity = this
        .findBankAccount(bankAccount);

    final javax.persistence.TypedQuery<com.dimediary.model.entities.TransactionEntity> query = this.entityManager
        .createNamedQuery(
            com.dimediary.model.entities.TransactionEntity.TRANSACTIONS_BETWEEN,
            com.dimediary.model.entities.TransactionEntity.class)
        .setParameter("bankAccount", bankAccountEntity).setParameter("dateFrom", dateFrom)
        .setParameter("dateUntil", dateUntil);

    transactions = query.getResultList();

    return this.entitiesToDomains(transactions);
  }

  @Override
  public java.util.List<Transaction> getTransactions(final BankAccount bankAccount) {
    Validate.notNull(bankAccount);

    com.dimediary.model.repositories.TransactionRepoImpl.log
        .info("getTransactions for bank account: " + bankAccount.getName());

    final com.dimediary.model.entities.BankAccountEntity bankAccountEntity = this
        .findBankAccount(bankAccount);

    final java.util.List<com.dimediary.model.entities.TransactionEntity> transactions = this.entityManager
        .createNamedQuery(com.dimediary.model.entities.TransactionEntity.ALL_ACCOUNT_TRANSACTIONS,
            com.dimediary.model.entities.TransactionEntity.class)
        .setParameter("bankAccount", bankAccountEntity).getResultList();
    return this.entitiesToDomains(transactions);
  }

  @Override
  public java.util.List<Transaction> getTransactions(final BankAccount bankAccount,
      final java.time.LocalDate date) {
    Validate.notNull(bankAccount);
    Validate.notNull(date);

    com.dimediary.model.repositories.TransactionRepoImpl.log
        .info("getTransactions for bank account: " + bankAccount.getName() + " at date: " + date
            .toString());

    final com.dimediary.model.entities.BankAccountEntity bankAccountEntity = this
        .findBankAccount(bankAccount);

    final java.util.List<com.dimediary.model.entities.TransactionEntity> transactions = this.entityManager
        .createNamedQuery(
            com.dimediary.model.entities.TransactionEntity.TRANSACTIONS_AT_DAY,
            com.dimediary.model.entities.TransactionEntity.class)
        .setParameter("bankAccount", bankAccountEntity).setParameter("date", date).getResultList();
    return this.entitiesToDomains(transactions);
  }

  @Override
  public java.util.List<Transaction> getTransactionsFromDate(
      final ContinuousTransaction continuousTransaction,
      final java.time.LocalDate date) {
    Validate.notNull(continuousTransaction);
    Validate.notNull(date);

    com.dimediary.model.repositories.TransactionRepoImpl.log
        .info(
            "getTransactionsFromDate for continuous transaction: " + continuousTransaction.getName()
                + " ("
                + continuousTransaction.getId() + ") " + " at date: " + date.toString());

    final com.dimediary.model.entities.ContinuousTransactionEntity continuousTransactionEntity = this
        .findContinuousTransaction(continuousTransaction);

    final java.util.List<com.dimediary.model.entities.TransactionEntity> transactions = this.entityManager
        .createNamedQuery("ContinuousTransansactionFromDate",
            com.dimediary.model.entities.TransactionEntity.class)
        .setParameter("continuousTransaction", continuousTransactionEntity)
        .setParameter("date", date)
        .getResultList();
    return this.entitiesToDomains(transactions);
  }

  @Override
  public java.util.List<Transaction> getTransactionsUntil(
      final ContinuousTransaction continuousTransaction,
      final java.time.LocalDate dateUntil) {
    Validate.notNull(continuousTransaction);
    Validate.notNull(dateUntil);

    com.dimediary.model.repositories.TransactionRepoImpl.log
        .info(
            "getTransactionsFromDate for continuous transaction: " + continuousTransaction.getName()
                + " ("
                + continuousTransaction.getId() + ") " + " until at date: " + dateUntil.toString());

    final com.dimediary.model.entities.ContinuousTransactionEntity continuousTransactionEntity = this
        .findContinuousTransaction(continuousTransaction);

    final java.util.List<com.dimediary.model.entities.TransactionEntity> transactions = this.entityManager
        .createNamedQuery(
            com.dimediary.model.entities.TransactionEntity.CONTINUOUS_TRANSANSACTION_UNTIL_DATE,
            com.dimediary.model.entities.TransactionEntity.class)
        .setParameter("continuousTransaction", continuousTransactionEntity)
        .setParameter("date", dateUntil)
        .getResultList();
    return this.entitiesToDomains(transactions);
  }

  @Override
  public java.util.List<Transaction> getTransactions(
      final ContinuousTransaction continuousTransaction) {
    Validate.notNull(continuousTransaction);

    com.dimediary.model.repositories.TransactionRepoImpl.log
        .info("getTransactions for continuous transaction: " + continuousTransaction.getName()
            + " (" + continuousTransaction.getId() + ") ");

    final com.dimediary.model.entities.ContinuousTransactionEntity continuousTransactionEntity = this
        .findContinuousTransaction(continuousTransaction);

    final java.util.List<com.dimediary.model.entities.TransactionEntity> transactions = this.entityManager
        .createNamedQuery(com.dimediary.model.entities.TransactionEntity.CONTINUOUS_TRANSACTIONS,
            com.dimediary.model.entities.TransactionEntity.class)
        .setParameter("continuousTransaction", continuousTransactionEntity).getResultList();
    return this.entitiesToDomains(transactions);
  }

  @Override
  public java.util.List<Transaction> getTransactionsWithoutAccount(
      final java.time.LocalDate dateFrom, final java.time.LocalDate dateUntil) {
    Validate.notNull(dateFrom);
    Validate.notNull(dateUntil);

    com.dimediary.model.repositories.TransactionRepoImpl.log
        .info("getTransactionsWithoutAccount from date: " + dateFrom.toString() + " until date: "
            + dateUntil.toString());
    final java.util.List<com.dimediary.model.entities.TransactionEntity> transactions = this.entityManager
        .createNamedQuery(
            com.dimediary.model.entities.TransactionEntity.TRANSACTIONS_WITHOUT_ACCOUNT_BETWEEN,
            com.dimediary.model.entities.TransactionEntity.class)
        .setParameter("dateFrom", dateFrom).setParameter("dateUntil", dateUntil).getResultList();
    return this.entitiesToDomains(transactions);
  }

  @Override
  public java.util.List<Transaction> getTrandactionsWithoutAccount(final java.time.LocalDate date) {
    Validate.notNull(date);

    com.dimediary.model.repositories.TransactionRepoImpl.log
        .info("getTransactionsWithoutAccount at date: " + date.toString());
    final java.util.List<com.dimediary.model.entities.TransactionEntity> transactions = this.entityManager
        .createNamedQuery(
            com.dimediary.model.entities.TransactionEntity.TRANSACTIONS_WITHOUT_ACCOUNT,
            com.dimediary.model.entities.TransactionEntity.class)
        .setParameter("date", date).getResultList();
    return this.entitiesToDomains(transactions);
  }

  @Override
  public Transaction persistTransaction(final Transaction transaction) {
    Validate.notNull(transaction, "Transaction must not be null");
    com.dimediary.model.repositories.TransactionRepoImpl.log
        .info("persist transaction: " + transaction.getId());

    try {

      com.dimediary.model.entities.TransactionEntity transactionEntityToSave = this
          .domainToEntity(transaction);
      if (this.findTransaction(transaction) == null) {
        this.entityManager.persist(transactionEntityToSave);
        this.entityManager.refresh(transactionEntityToSave);
      } else {
        transactionEntityToSave = this.entityManager.merge(transactionEntityToSave);
      }

      return this.transactionTransformer.transactionEntityToTransaction(transactionEntityToSave);


    } catch (final Exception e) {
      com.dimediary.model.repositories.TransactionRepoImpl.log
          .error("can't persist transaction", e);
      throw e;
    }

  }

  @Override
  public void persistTransactions(final java.util.List<Transaction> transactions) {
    Validate.notNull(transactions, "Transactions must not be null");
    if (transactions.isEmpty()) {
      return;
    }

    for (final Transaction transaction : transactions) {
      this.persistTransaction(transaction);
    }

  }

  @Override
  public void delete(final Transaction transaction) {
    Validate.notNull(transaction);

    com.dimediary.model.repositories.TransactionRepoImpl.log
        .info("delete Transaction: " + transaction.getId());

    try {
      final com.dimediary.model.entities.TransactionEntity transactionEntity = this
          .findTransaction(transaction);

      if (transactionEntity != null) {
        this.entityManager.remove(transactionEntity);
      }

    } catch (final Exception e) {
      com.dimediary.model.repositories.TransactionRepoImpl.log
          .error("can't delete transaction: " + transaction.getId(), e);
      throw e;
    }

  }

  @Override
  public void deleteTransactions(final java.util.List<Transaction> transactions) {
    Validate.notNull(transactions, "transactions must not be null");
    if (transactions.isEmpty()) {
      return;

    }

    for (final Transaction transaction : transactions) {
      this.delete(transaction);
    }

  }

  private java.util.List<Transaction> entitiesToDomains(
      final java.util.List<com.dimediary.model.entities.TransactionEntity> transactions) {
    return transactions.stream()
        .map((transaction) -> this.transactionTransformer
            .transactionEntityToTransaction(transaction))
        .collect(java.util.stream.Collectors.toList());
  }

  private com.dimediary.model.entities.TransactionEntity findTransaction(
      final Transaction transaction) {
    if (transaction != null && transaction.getId() != null) {
      return this.entityManager.find(
          com.dimediary.model.entities.TransactionEntity.class, transaction.getId());
    } else {
      return null;
    }

  }

  private com.dimediary.model.entities.TransactionEntity domainToEntity(
      final Transaction transaction) {
    return this.transactionTransformer.transactionToTransactionEntity(transaction,
        this.findBankAccount(transaction), this.findContinuousTransaction(transaction),
        this.findCategory(transaction));
  }

  private com.dimediary.model.entities.ContinuousTransactionEntity findContinuousTransaction(
      final Transaction transaction) {
    if (transaction != null && transaction.getContinuousTransaction() != null) {
      return this.findContinuousTransaction(transaction.getContinuousTransaction());
    } else {
      return null;
    }
  }

  private com.dimediary.model.entities.ContinuousTransactionEntity findContinuousTransaction(
      final ContinuousTransaction continuousTransaction) {
    if (continuousTransaction != null && continuousTransaction.getId() != null) {
      return this.entityManager.find(
          com.dimediary.model.entities.ContinuousTransactionEntity.class,
          continuousTransaction.getId());
    } else {
      return null;
    }
  }

  private com.dimediary.model.entities.CategoryEntity findCategory(final Transaction transaction) {
    if (transaction.getCategory() != null && transaction.getCategory().getName() != null) {
      return this.entityManager.find(
          com.dimediary.model.entities.CategoryEntity.class,
          transaction.getCategory().getName());
    } else {
      return null;
    }
  }

  private com.dimediary.model.entities.BankAccountEntity findBankAccount(
      final Transaction transaction) {
    if (transaction != null && transaction.getBankAccount() != null) {
      return this.findBankAccount(transaction.getBankAccount());
    } else {
      return null;
    }
  }

  private com.dimediary.model.entities.BankAccountEntity findBankAccount(
      final BankAccount bankAccount) {
    if (bankAccount != null && bankAccount.getName() != null) {
      return this.entityManager.find(
          com.dimediary.model.entities.BankAccountEntity.class, bankAccount.getName());
    } else {
      return null;
    }
  }

}
