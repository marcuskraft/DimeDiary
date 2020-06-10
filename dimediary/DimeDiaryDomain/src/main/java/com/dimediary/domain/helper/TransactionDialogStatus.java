package com.dimediary.domain.helper;

import com.dimediary.domain.BankAccount;
import com.dimediary.domain.Category;
import com.dimediary.domain.ContinuousTransaction;
import com.dimediary.domain.Transaction;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.dmfs.rfc5545.recur.RecurrenceRule;

@Getter
@Builder
public class TransactionDialogStatus {

  private final Transaction transaction;
  private final ContinuousTransaction continuousTransaction;

  private final String name;
  private final Double amount;

  @Setter
  private LocalDate date;
  private final Category category;
  private final BankAccount bankaccount;

  private final boolean noCategory;
  private final boolean noBankaccount;

  private final boolean fixCost;

  private final RecurrenceRule recurrenceRule;

  private final LocalDate changeFromDate;

}
