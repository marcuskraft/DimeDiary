package com.dimediary.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 5747032574494786944L;
  private Integer id;
  private String name;
  private Double amount;
  private BankAccount bankAccount;
  private Category category;
  private LocalDate date;
  private LocalDateTime timestamp;
  private ContinuousTransaction continuousTransaction;
  private Boolean fixCost;

  public Transaction getCopy() {
    final Transaction transaction = new Transaction();
    transaction.setAmount(this.getAmount());
    transaction.setBankAccount(this.getBankAccount());
    transaction.setCategory(this.getCategory());
    transaction.setContinuousTransaction(this.getContinuousTransaction());
    transaction.setDate(this.getDate());
    transaction.setName(this.getName());
    transaction.setFixCost(this.getFixCost());
    return transaction;
  }

}
