package com.dimediary.persistence.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "continuous_transaction")
@Data
public class ContinuousTransactionEntity implements Serializable,
    Comparable<ContinuousTransactionEntity> {


  private static final long serialVersionUID = 1064986628363384286L;

  @Id
  private String id;

  private LocalDateTime timestamp;
  private String name;
  private Integer amountEuroCent;
  private LocalDate dateBegin;

  @ManyToOne
  @JoinColumn(name = "bank_account_id")
  private BankAccountEntity bankAccount;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private CategoryEntity category;

  private String recurrenceRule;

  private Boolean fixCost;

  @OneToMany
  @JoinColumn(name = "CONTINUOUS_TRANSACTION_ID")
  private Collection<RecurrenceExceptionEntity> exceptions;

  @OneToMany
  @JoinColumn(name = "CONTINUOUS_TRANSACTION_ID")
  private Collection<RecurrenceExtraInstanceEntity> extraInstances;


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
