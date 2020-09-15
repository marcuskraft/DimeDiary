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
public class Transaction implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 5747032574494786944L;
  private UUID id;
  private String name;
  private Integer amount;
  private BankAccount bankAccount;
  private Category category;
  private LocalDate date;
  private LocalDateTime timestamp;
  private ContinuousTransaction continuousTransaction;
  private Boolean fixCost;

}
