package com.dimediary.model.entities;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;


@Entity
@Table(name = "bank_account")
@Data
public class BankAccountEntity implements Serializable {


  private static final long serialVersionUID = 7788504506056118005L;

  @Id
  private String id;
  private String name;
  private String bankName;
  private String iban;
  private String bic;

  @ManyToOne
  @JoinColumn(name = "bank_account_category_id")
  private BankAccountCategoryEntity bankAccountCategory;

  private LocalDate dateStartBalance;
  private Integer startBalanceEuroCent;

}
