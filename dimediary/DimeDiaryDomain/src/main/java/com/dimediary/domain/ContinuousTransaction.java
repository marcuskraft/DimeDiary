package com.dimediary.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContinuousTransaction implements Serializable {


  private static final long serialVersionUID = 2371735569007687455L;
  private UUID id;
  private LocalDateTime timestamp;
  private String name;
  private Integer amountEuroCent;
  private LocalDate dateBegin;
  private BankAccount bankAccount;
  private Category category;
  private String recurrenceRule;


  private Boolean fixCost;

  public Transaction createTransaction(final LocalDate date) {
    final Transaction transaction = new Transaction();
    transaction.setAmountEuroCent(this.getAmountEuroCent());
    transaction.setBankAccount(this.getBankAccount());
    transaction.setCategory(this.getCategory());
    transaction.setContinuousTransaction(this);
    transaction.setDate(date);
    transaction.setName(this.getName());
    transaction.setFixCost(this.getFixCost());
    return transaction;
  }

  public ContinuousTransaction getCopy() {
    final ContinuousTransaction continuousTransactionCopy = new ContinuousTransaction();

    continuousTransactionCopy.setAmountEuroCent(this.getAmountEuroCent());
    continuousTransactionCopy.setBankAccount(this.getBankAccount());
    continuousTransactionCopy.setCategory(this.getCategory());
    continuousTransactionCopy.setDateBegin(this.getDateBegin());
    continuousTransactionCopy.setName(this.getName());
    continuousTransactionCopy.setRecurrenceRule(this.getRecurrenceRule());
    continuousTransactionCopy.setFixCost(this.getFixCost());

    return continuousTransactionCopy;

  }

}
