package com.dimediary.persistence.entities;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;


@Entity
@Table(name = "balances")
@Data
public class BalanceEntity implements Serializable {


  private static final long serialVersionUID = -8067932338253105032L;


  @Id
  private String id;
  private LocalDate date;

  @ManyToOne
  @JoinColumn(name = "bank_account_id")
  private BankAccountEntity bankAccount;

  private Integer balanceEuroCent;


}
