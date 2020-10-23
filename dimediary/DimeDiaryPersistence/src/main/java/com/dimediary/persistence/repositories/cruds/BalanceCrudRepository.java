package com.dimediary.persistence.repositories.cruds;

import com.dimediary.persistence.entities.BalanceEntity;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface BalanceCrudRepository extends CrudRepository<BalanceEntity, String> {

  List<BalanceEntity> getBalanceEntitiesByBankAccountIdAndDateIsGreaterThanEqual(
      String bankAccountId,
      LocalDate localDate);

  BalanceEntity findFirstByBankAccountIdAndDateBeforeOrderByDateDesc(
      String bankAccountId, LocalDate localDate);

  BalanceEntity findFirstByBankAccountIdOrderByDateDesc(String bankAccountId);

  List<BalanceEntity> getAllByBankAccountId(String bankAccountId);

  void deleteByBankAccountIdAndDate(String bankAccountId, LocalDate localDate);

}
