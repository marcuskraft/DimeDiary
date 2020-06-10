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
public class BalanceHistory implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -322723363175753187L;
  private BankAccount bankAccount;
  private LocalDate date;
  private Double amount;

  public void addAmount(final Double amount) {
    this.amount += amount;
  }

}
