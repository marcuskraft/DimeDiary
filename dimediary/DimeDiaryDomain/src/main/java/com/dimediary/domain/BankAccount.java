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
public class BankAccount implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -2683040968237992882L;
  private String name;
  private String bankName;
  private String iban;
  private String bic;
  private BankAccountCategory bankAccountCategory;
  private LocalDate dateStartBalance;
  private Double startBalance;

}
