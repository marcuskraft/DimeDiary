package com.dimediary.model.entities;

import com.dimediary.domain.helper.AmountUtils;
import javax.persistence.Column;

@javax.persistence.Entity
@javax.persistence.Table(name = "CONTINUOUS_TRANSACTION")
@javax.persistence.NamedQueries({
    @javax.persistence.NamedQuery(name = com.dimediary.model.entities.ContinuousTransactionEntity.CONTINUOUS_TRANSACTION_FOR_BANK_ACCOUNT, query = "from ContinuousTransactionEntity WHERE bankAccount = :bankAccount"),
    @javax.persistence.NamedQuery(name = com.dimediary.model.entities.ContinuousTransactionEntity.DELETE_ALL_CONTINUOUS_TRANSACTIONS, query = "DELETE FROM ContinuousTransactionEntity")})
@lombok.Data
public class ContinuousTransactionEntity implements java.io.Serializable,
    Comparable<ContinuousTransactionEntity> {

  public static final String DELETE_ALL_CONTINUOUS_TRANSACTIONS = "DELETE_ALL_CONTINUOUS_TRANSACTIONS";

  public static final String CONTINUOUS_TRANSACTION_FOR_BANK_ACCOUNT = "continuousTransactionForBankAccount";

  /**
   *
   */
  private static final long serialVersionUID = 1064986628363384286L;

  @javax.persistence.Id
  @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  @javax.persistence.Column(name = "ID")
  private Integer id;

  @javax.persistence.Column(name = "TIMESTAMP", updatable = true)
  private java.time.LocalDateTime timestamp;

  @javax.persistence.Column(name = "NAME")
  private String name;

  @javax.persistence.Column(name = "AMOUNT")
  private Double amount;

  @javax.persistence.Column(name = "DATE_BEGINN")
  private java.time.LocalDate dateBeginn;

  @javax.persistence.ManyToOne
  @javax.persistence.JoinColumn(name = "BANKACCOUNT_NAME")
  private com.dimediary.model.entities.BankAccountEntity bankAccount;

  @javax.persistence.ManyToOne
  @javax.persistence.JoinColumn(name = "CATEGORY_NAME")
  private com.dimediary.model.entities.CategoryEntity category;

  @Column(name = "RECURRENCERULE")
  private String recurrenceRule;

  @Column(name = "FIXCOST")
  private Boolean fixCost;

  public Double getAmount() {
    return AmountUtils.round(this.amount);
  }

  public void setAmount(final Double amount) {
    this.amount = AmountUtils.round(amount);
  }

  @javax.persistence.PrePersist
  private void setTimestamp() {
    this.timestamp = java.time.LocalDateTime.now();
  }

  public void setTimestamp(final java.time.LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public int compareTo(final com.dimediary.model.entities.ContinuousTransactionEntity other) {
    if (other == null) {
      return -1;
    }
    return this.dateBeginn.compareTo(other.getDateBeginn());
  }

  public Boolean getFixCost() {
    if (this.fixCost == null && this.category != null) {
      return this.category.getFixCost();
    }
    return this.fixCost;
  }

}
