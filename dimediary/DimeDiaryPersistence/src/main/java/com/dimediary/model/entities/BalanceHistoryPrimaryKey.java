package com.dimediary.model.entities;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.Data;

/**
 * primary key class for BalanceHistory
 *
 * @author eyota
 */
@Data
public class BalanceHistoryPrimaryKey implements Serializable {


  private static final long serialVersionUID = -4539881778212051537L;
  private LocalDate date;
  private BankAccountEntity bankAccount;


}
