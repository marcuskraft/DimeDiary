package com.dimediary.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Balance implements Serializable {


  private static final long serialVersionUID = -322723363175753187L;
  private UUID id;
  private BankAccount bankAccount;
  private LocalDate date;
  private Integer balanceEuroCent;

  public void addAmount(final Integer amountEuroCent) {
    this.balanceEuroCent += amountEuroCent;
  }

}
