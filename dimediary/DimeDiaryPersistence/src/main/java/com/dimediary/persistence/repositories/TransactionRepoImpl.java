package com.dimediary.persistence.repositories;

import com.dimediary.domain.BankAccount;
import com.dimediary.domain.ContinuousTransaction;
import com.dimediary.domain.Transaction;
import com.dimediary.persistence.converter.TransactionTransformer;
import com.dimediary.persistence.entities.BankAccountEntity;
import com.dimediary.persistence.entities.CategoryEntity;
import com.dimediary.persistence.entities.ContinuousTransactionEntity;
import com.dimediary.persistence.entities.TransactionEntity;
import com.dimediary.persistence.repositories.cruds.BankAccountCrudRepository;
import com.dimediary.persistence.repositories.cruds.CategoryCrudRepository;
import com.dimediary.persistence.repositories.cruds.ContinuousTransactionCrudRepository;
import com.dimediary.persistence.repositories.cruds.TransactionCrudRepository;
import com.dimediary.persistence.repositories.utils.RepoUtils;
import com.dimediary.port.out.TransactionRepo;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class TransactionRepoImpl implements TransactionRepo {

  private final TransactionTransformer transactionTransformer;
  private final TransactionCrudRepository transactionCrudRepository;
  private final CategoryCrudRepository categoryCrudRepository;
  private final BankAccountCrudRepository bankAccountCrudRepository;
  private final ContinuousTransactionCrudRepository continuousTransactionCrudRepository;

  @Autowired
  public TransactionRepoImpl(final TransactionTransformer transactionTransformer,
      final TransactionCrudRepository transactionCrudRepository,
      final CategoryCrudRepository categoryCrudRepository,
      final BankAccountCrudRepository bankAccountCrudRepository,
      final ContinuousTransactionCrudRepository continuousTransactionCrudRepository) {
    this.transactionTransformer = transactionTransformer;
    this.transactionCrudRepository = transactionCrudRepository;
    this.categoryCrudRepository = categoryCrudRepository;
    this.bankAccountCrudRepository = bankAccountCrudRepository;
    this.continuousTransactionCrudRepository = continuousTransactionCrudRepository;
  }


  @Override
  public Transaction getTransaction(final UUID transactionId) {
    return this.transactionTransformer.transactionEntityToTransaction(this.transactionCrudRepository
        .findById(transactionId.toString())
        .orElseThrow(() -> RepoUtils.getNoResultException("transaction", transactionId)));
  }

  @Override
  public List<Transaction> getTransactions(final LocalDate dateFrom, final LocalDate dateUntil,
      final BankAccount bankAccount) {
    Validate.notNull(dateFrom);
    Validate.notNull(dateUntil);
    Validate.notNull(bankAccount);
    Validate.notNull(bankAccount.getId());

    TransactionRepoImpl.log
        .info("getTransactions from date: " + dateFrom.toString() + " until date: "
            + dateUntil.toString() + " for bank account: " + bankAccount.getName());

    return this.transactionCrudRepository
        .getAllByBankAccountIdAndDateIsGreaterThanEqualAndDateIsLessThanEqual(
            bankAccount.getId().toString(),
            dateFrom, dateUntil).stream()
        .map(this.transactionTransformer::transactionEntityToTransaction).collect(
            Collectors.toList());
  }

  @Override
  public List<Transaction> getTransactions(final BankAccount bankAccount,
      final LocalDate date) {
    Validate.notNull(bankAccount);
    Validate.notNull(date);

    TransactionRepoImpl.log
        .info("getTransactions for bank account: " + bankAccount.getName() + " at date: " + date
            .toString());

    return this.transactionCrudRepository
        .getAllByBankAccountIdAndDateOrderByTimestamp(bankAccount.getId().toString(), date).stream()
        .map(
            this.transactionTransformer::transactionEntityToTransaction).collect(
            Collectors.toList());

  }

  @Override
  public List<Transaction> getTransactionsFromDate(
      final ContinuousTransaction continuousTransaction,
      final LocalDate date) {
    Validate.notNull(continuousTransaction);
    Validate.notNull(continuousTransaction.getId());
    Validate.notNull(date);

    TransactionRepoImpl.log.info(
        "getTransactionsFromDate for continuous transaction: " + continuousTransaction.getName()
            + " (" + continuousTransaction.getId() + ") " + " at date: " + date.toString());

    return this.transactionCrudRepository
        .getAllByContinuousTransactionIdAndDateIsGreaterThanEqual(
            continuousTransaction.getId().toString(),
            date).stream().map(this.transactionTransformer::transactionEntityToTransaction).collect(
            Collectors.toList());
  }

  @Override
  public List<Transaction> getTransactionsUntil(
      final ContinuousTransaction continuousTransaction,
      final LocalDate dateUntil) {
    Validate.notNull(continuousTransaction);
    Validate.notNull(dateUntil);

    TransactionRepoImpl.log.info(
        "getTransactionsFromDate for continuous transaction: " + continuousTransaction.getName()
            + " (" + continuousTransaction.getId() + ") " + " until at date: " + dateUntil
            .toString());

    return this.transactionCrudRepository
        .getAllByContinuousTransactionIdAndDateIsLessThanEqual(
            continuousTransaction.getId().toString(),
            dateUntil).stream().map(this.transactionTransformer::transactionEntityToTransaction)
        .collect(Collectors.toList());
  }

  @Override
  public List<Transaction> getTransactions(final UUID id) {

    TransactionRepoImpl.log
        .info("getTransactions for continuous transaction: " + id);

    return this.transactionCrudRepository
        .getAllByContinuousTransactionIdOrderByDate(id.toString())
        .stream()
        .map(this.transactionTransformer::transactionEntityToTransaction)
        .collect(Collectors.toList());
  }

  @Override
  public List<Transaction> getTransactionsWithoutAccount(
      final LocalDate dateFrom, final LocalDate dateUntil) {
    Validate.notNull(dateFrom);
    Validate.notNull(dateUntil);

    TransactionRepoImpl.log
        .info("getTransactionsWithoutAccount from date: " + dateFrom.toString() + " until date: "
            + dateUntil.toString());
    return this.transactionCrudRepository
        .getAllByBankAccountNullAndDateIsGreaterThanEqualAndDateIsLessThanEqual(dateFrom, dateUntil)
        .stream().map(this.transactionTransformer::transactionEntityToTransaction)
        .collect(Collectors.toList());
  }

  @Override
  public Transaction persistTransaction(final Transaction transaction) {
    Validate.notNull(transaction, "Transaction must not be null");
    TransactionRepoImpl.log.info("persist transaction: " + transaction.getId());

    if (transaction.getId() == null) {
      transaction.setId(UUID.randomUUID());
    }

    TransactionEntity transactionEntityToSave = this
        .domainToEntity(transaction);

    transactionEntityToSave = this.transactionCrudRepository.save(transactionEntityToSave);

    return this.transactionTransformer.transactionEntityToTransaction(transactionEntityToSave);
  }

  @Override
  public void delete(final Transaction transaction) {
    Validate.notNull(transaction);
    Validate.notNull(transaction.getId());

    TransactionRepoImpl.log
        .info("delete Transaction: " + transaction.getId());

    this.transactionCrudRepository.deleteById(transaction.getId().toString());

  }

  private TransactionEntity domainToEntity(
      final Transaction transaction) {
    return this.transactionTransformer.transactionToTransactionEntity(transaction,
        this.findBankAccount(transaction), this.findContinuousTransaction(transaction),
        this.findCategory(transaction));
  }

  private ContinuousTransactionEntity findContinuousTransaction(
      final Transaction transaction) {
    if (transaction != null && transaction.getContinuousTransaction() != null) {
      return this.findContinuousTransaction(transaction.getContinuousTransaction());
    } else {
      return null;
    }
  }

  private ContinuousTransactionEntity findContinuousTransaction(
      final ContinuousTransaction continuousTransaction) {
    if (continuousTransaction != null && continuousTransaction.getId() != null) {
      return this.continuousTransactionCrudRepository
          .findById(continuousTransaction.getId().toString())
          .orElseThrow(() -> RepoUtils.getNoResultException("continuousTransaction",
              continuousTransaction.getId()));
    } else {
      return null;
    }
  }


  private CategoryEntity findCategory(final Transaction transaction) {
    if (transaction.getCategory() != null) {
      return this.categoryCrudRepository
          .findById(transaction.getCategory().getId().toString()).orElseThrow(
              () -> RepoUtils.getNoResultException("category", transaction.getCategory().getId()));
    } else {
      return null;
    }
  }

  private BankAccountEntity findBankAccount(
      final Transaction transaction) {
    if (transaction != null && transaction.getBankAccount() != null) {
      return this.findBankAccount(transaction.getBankAccount());
    } else {
      return null;
    }
  }

  private BankAccountEntity findBankAccount(
      final BankAccount bankAccount) {
    if (bankAccount != null) {
      return this.bankAccountCrudRepository.findById(bankAccount.getId().toString())
          .orElseThrow(() -> RepoUtils.getNoResultException("bank account", bankAccount.getId()));
    } else {
      return null;
    }
  }

}
