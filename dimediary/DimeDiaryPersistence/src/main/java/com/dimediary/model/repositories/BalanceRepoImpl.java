package com.dimediary.model.repositories;

import com.dimediary.domain.Balance;
import com.dimediary.domain.BankAccount;
import com.dimediary.model.converter.BalanceTransformer;
import com.dimediary.model.entities.BalanceEntity;
import com.dimediary.model.entities.BankAccountEntity;
import com.dimediary.model.repositories.cruds.BalanceCrudRepository;
import com.dimediary.model.repositories.cruds.BankAccountCrudRepository;
import com.dimediary.model.repositories.utils.RepoUtils;
import com.dimediary.port.out.BalanceRepo;
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
class BalanceRepoImpl implements BalanceRepo {


  private final BalanceTransformer balanceTransformer;
  private final BalanceCrudRepository balanceCrudRepository;
  private final BankAccountCrudRepository bankAccountCrudRepository;

  @Autowired
  public BalanceRepoImpl(final BalanceTransformer balanceTransformer,
      final BalanceCrudRepository balanceCrudRepository,
      final BankAccountCrudRepository bankAccountCrudRepository) {
    this.balanceTransformer = balanceTransformer;
    this.balanceCrudRepository = balanceCrudRepository;
    this.bankAccountCrudRepository = bankAccountCrudRepository;
  }


  @Override
  public List<Balance> getBalanceHistoriesAfterDate(final BankAccount bankAccount,
      final LocalDate date) {
    Validate.notNull(bankAccount);
    Validate.notNull(date);
    BalanceRepoImpl.log
        .info("getBalanceHistoriesAfterDate for bank account: " + bankAccount.getName()
            + " and after date: "
            + date.toString());

    return this.balanceCrudRepository
        .getBalanceEntitiesByBankAccountIdAndDateIsGreaterThanEqual(bankAccount.getId().toString(),
            date)
        .stream().map(this.balanceTransformer::balanceHistoryEntityToBalanceHistory)
        .collect(Collectors.toList());
  }

  @Override
  public Balance getBalanceHistoryBefore(final BankAccount bankAccount,
      final LocalDate date) {
    Validate.notNull(bankAccount);
    Validate.notNull(bankAccount.getId());
    Validate.notNull(date);

    BalanceRepoImpl.log
        .info("getLastBalanceHistory for bank account: {} and before: {}", bankAccount.getName(),
            date);

    return this.entityToDomain(this.balanceCrudRepository
        .findFirstByBankAccountIdAndDateBeforeOrderByDateDesc(bankAccount.getId().toString(),
            date));
  }

  @Override
  public Balance getLastBalanceHistory(final BankAccount bankAccount) {
    Validate.notNull(bankAccount);

    BalanceRepoImpl.log
        .info("getLastBalanceHistory for bank account: " + bankAccount.getName());

    return this.entityToDomain(
        this.balanceCrudRepository
            .findFirstByBankAccountIdOrderByDateDesc(bankAccount.getId().toString()));
  }


  @Override
  public void persistBalanceHistories(final List<Balance> balanceHistories) {
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

    final List<Balance> balanceHistories = this.getBalanceHistories(bankAccount);
    if (!balanceHistories.isEmpty()) {
      this.deleteBalanceHistories(balanceHistories);
    }

  }

  private Balance entityToDomain(
      final BalanceEntity balanceEntity) {
    return this.balanceTransformer.balanceHistoryEntityToBalanceHistory(balanceEntity);
  }

  private BankAccountEntity findBankAccount(final BankAccount bankAccount) {
    return this.bankAccountCrudRepository.findById(bankAccount.getId().toString())
        .orElseThrow(() -> RepoUtils.getNoResultException("bank account", bankAccount.getId()));
  }

  private void delete(final Balance balance) {
    Validate.notNull(balance);
    Validate.notNull(balance.getBankAccount());
    Validate.notNull(balance.getBankAccount().getId());
    Validate.notNull(balance.getDate());

    BalanceRepoImpl.log
        .info("delete BalanceHistory for bank account: " + balance.getBankAccount().getBankName()
            + " and date: " + balance.getDate());

    this.balanceCrudRepository
        .deleteByBankAccountIdAndDate(balance.getBankAccount().getId().toString(),
            balance.getDate());
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
    BalanceRepoImpl.log
        .info("persist BalanceHistory for bank account: " + balance.getBankAccount()
            + " and date: "
            + balance.getDate());

    final BalanceEntity balanceEntity = this.balanceTransformer
        .balanceHistoryToBalanceHistoryEntity(balance,
            this.findBankAccount(balance.getBankAccount()));
    if (balanceEntity.getId() == null) {
      balanceEntity.setId(UUID.randomUUID().toString());
    }

    this.balanceCrudRepository.save(balanceEntity);
  }

  private List<Balance> getBalanceHistories(final BankAccount bankAccount) {
    Validate.notNull(bankAccount);
    Validate.notNull(bankAccount.getId());
    BalanceRepoImpl.log
        .info("getBalanceHistories for bank account: " + bankAccount.getName());
    return this.balanceCrudRepository.getAllByBankAccountId(bankAccount.getId().toString()).stream()
        .map(
            this.balanceTransformer::balanceHistoryEntityToBalanceHistory).collect(
            Collectors.toList());
  }

}
