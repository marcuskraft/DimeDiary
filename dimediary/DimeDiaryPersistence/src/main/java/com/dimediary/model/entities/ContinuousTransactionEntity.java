package com.dimediary.model.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "continuous_transaction")
@NamedQueries({
    @NamedQuery(name = ContinuousTransactionEntity.CONTINUOUS_TRANSACTION_FOR_BANK_ACCOUNT, query = "from ContinuousTransactionEntity WHERE bankAccount = :bankAccount"),
    @NamedQuery(name = ContinuousTransactionEntity.DELETE_ALL_CONTINUOUS_TRANSACTIONS, query = "DELETE FROM ContinuousTransactionEntity")})
@Data
public class ContinuousTransactionEntity implements Serializable,
    Comparable<ContinuousTransactionEntity> {

  public static final String DELETE_ALL_CONTINUOUS_TRANSACTIONS = "DELETE_ALL_CONTINUOUS_TRANSACTIONS";

  public static final String CONTINUOUS_TRANSACTION_FOR_BANK_ACCOUNT = "continuousTransactionForBankAccount";

  /**
   *
   */
  private static final long serialVersionUID = 1064986628363384286L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  private LocalDateTime timestamp;
  private String name;
  private Integer amount;
  private LocalDate dateBegin;

  @ManyToOne
  @JoinColumn(name = "bank_account_id")
  private BankAccountEntity bankAccount;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private CategoryEntity category;

  private String recurrenceRule;

  private Boolean fixCost;


  @PrePersist
  private void setTimestamp() {
    this.timestamp = java.time.LocalDateTime.now();
  }

  @Override
  public int compareTo(final ContinuousTransactionEntity other) {
    if (other == null) {
      return -1;
    }
    return this.dateBegin.compareTo(other.getDateBegin());
  }

  public Boolean getFixCost() {
    if (this.fixCost == null && this.category != null) {
      return this.category.getFixCost();
    }
    return this.fixCost;
  }

}
