package com.dimediary.model.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "TRANSACTIONS", indexes = {
    @Index(columnList = "date", name = "transaction_date_hidx")})
@Data
public class TransactionEntity implements Serializable {


  private static final long serialVersionUID = -6570668384679595613L;

  @Id
  private String id;

  private String name;
  private Integer amountEuroCent;

  @ManyToOne
  @JoinColumn(name = "bank_account_id")
  private BankAccountEntity bankAccount;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private CategoryEntity category;

  private LocalDate date;

  private LocalDateTime timestamp;

  @ManyToOne
  @JoinColumn(name = "continuous_transaction_id")
  private ContinuousTransactionEntity continuousTransaction;

  private Boolean fixCost;

  @PrePersist
  private void setTimestamp() {
    this.timestamp = LocalDateTime.now();
  }


  public Boolean getFixCost() {
    if (this.fixCost == null && this.category != null) {
      return this.category.getFixCost();
    }
    return this.fixCost;
  }

  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer();
    buffer.append("name: ");
    buffer.append(this.name);
    buffer.append(" ");
    buffer.append("bankaccount: ");
    buffer.append(this.bankAccount.getName());
    buffer.append(" ");
    buffer.append("date: ");
    buffer.append(this.date);

    return buffer.toString();
  }

}
