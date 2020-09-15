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
public class BankAccount implements Serializable {


  private static final long serialVersionUID = 5769361494231104113L;
  private UUID id;
  private String name;
  private String bankName;
  private String iban;
  private String bic;
  private BankAccountCategory bankAccountCategory;
  private LocalDate dateStartBalance;
  private Integer startBalanceEuroCent;

}
