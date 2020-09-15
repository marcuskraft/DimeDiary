package com.dimediary.domain;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Balance implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -322723363175753187L;
  private BankAccount bankAccount;
  private LocalDate date;
  private Double balance;

  public void addAmount(final Double amount) {
    this.balance += amount;
  }

}
