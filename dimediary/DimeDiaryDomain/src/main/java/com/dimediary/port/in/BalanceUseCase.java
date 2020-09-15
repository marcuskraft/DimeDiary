package com.dimediary.port.in;

import com.dimediary.domain.Balance;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BalanceUseCase {


  enum BalanceAction {
    adding, deleting
  }

  List<Balance> getBalancesFollowingDays(UUID bankAccountId, LocalDate dateFrom,
      LocalDate dateUntil);


}
