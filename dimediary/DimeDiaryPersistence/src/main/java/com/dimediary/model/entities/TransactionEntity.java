package com.dimediary.model.entities;

import com.dimediary.domain.helper.AmountUtils;

@javax.persistence.NamedQueries({
    @javax.persistence.NamedQuery(name = com.dimediary.model.entities.TransactionEntity.TRANSACTIONS_BETWEEN, query =
        "from TransactionEntity t WHERE t.bankAccount = :bankAccount"
            + " AND t.date BETWEEN :dateFrom AND :dateUntil ORDER BY t.date"),
    @javax.persistence.NamedQuery(name = com.dimediary.model.entities.TransactionEntity.ALL_ACCOUNT_TRANSACTIONS, query = "from TransactionEntity t WHERE t.bankAccount = :bankAccount"),
    @javax.persistence.NamedQuery(name = com.dimediary.model.entities.TransactionEntity.TRANSACTIONS_AT_DAY, query = "from TransactionEntity t WHERE t.bankAccount = :bankAccount AND t.date = :date order by t.timestamp"),
    @javax.persistence.NamedQuery(name = com.dimediary.model.entities.TransactionEntity.CONTINUOUS_TRANSANSACTION_FROM_DATE, query =
        "from TransactionEntity t WHERE t.continuousTransaction = :continuousTransaction "
            + "AND t.date >= :date"),
    @javax.persistence.NamedQuery(name = com.dimediary.model.entities.TransactionEntity.CONTINUOUS_TRANSANSACTION_UNTIL_DATE, query =
        "from TransactionEntity t WHERE t.continuousTransaction = :continuousTransaction "
            + "AND t.date <= :date"),
    @javax.persistence.NamedQuery(name = com.dimediary.model.entities.TransactionEntity.CONTINUOUS_TRANSACTIONS, query = "from TransactionEntity t WHERE t.continuousTransaction = :continuousTransaction"),
    @javax.persistence.NamedQuery(name = com.dimediary.model.entities.TransactionEntity.TRANSACTIONS_WITHOUT_ACCOUNT_BETWEEN, query =
        "from TransactionEntity t where t.bankAccount is null and t.date BETWEEN :dateFrom"
            + " AND :dateUntil ORDER BY t.date"),
    @javax.persistence.NamedQuery(name = com.dimediary.model.entities.TransactionEntity.TRANSACTIONS_WITHOUT_ACCOUNT, query = "from TransactionEntity t where t.bankAccount is null and t.date = :date"),
    @javax.persistence.NamedQuery(name = com.dimediary.model.entities.TransactionEntity.DELETE_ALL_TRANSACTIONS, query = "DELETE FROM TransactionEntity")})
@javax.persistence.Entity
@javax.persistence.Table(name = "TRANSACTIONS", indexes = {
    @javax.persistence.Index(columnList = "date", name = "transaction_date_hidx")})
@lombok.Data
public class TransactionEntity implements java.io.Serializable {

  public static final String TRANSACTIONS_WITHOUT_ACCOUNT = "TransactionsWithoutAccount";

  public static final String TRANSACTIONS_WITHOUT_ACCOUNT_BETWEEN = "TransactionsWithoutAccountBetween";

  public static final String CONTINUOUS_TRANSACTIONS = "ContinuousTransactions";

  public static final String TRANSACTIONS_AT_DAY = "TransactionsAtDay";

  public static final String ALL_ACCOUNT_TRANSACTIONS = "allAccountTransactions";

  public static final String TRANSACTIONS_BETWEEN = "TransactionsBetween";

  public static final String CONTINUOUS_TRANSANSACTION_UNTIL_DATE = "ContinuousTransansactionUntilDate";

  public static final String CONTINUOUS_TRANSANSACTION_FROM_DATE = "ContinuousTransansactionFromDate";

  public static final String DELETE_ALL_TRANSACTIONS = "DELETE_ALL_TRANSACTIONS";

  /**
   *
   */
  private static final long serialVersionUID = -6570668384679595613L;

  @javax.persistence.Id
  @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  @javax.persistence.Column(name = "ID")
  private Integer id;

  @javax.persistence.Column(name = "NAME")
  private String name;

  @javax.persistence.Column(name = "AMOUNT")
  private Double amount;

  @javax.persistence.ManyToOne
  @javax.persistence.JoinColumn(name = "BANKACCOUNT_NAME")
  private com.dimediary.model.entities.BankAccountEntity bankAccount;

  @javax.persistence.ManyToOne
  @javax.persistence.JoinColumn(name = "CATEGORY_NAME")
  private com.dimediary.model.entities.CategoryEntity category;

  @javax.persistence.Column(name = "DATE")
  private java.time.LocalDate date;

  @javax.persistence.Column(name = "TIMESTAMP", updatable = true)
  private java.time.LocalDateTime timestamp;

  @javax.persistence.ManyToOne
  @javax.persistence.JoinColumn(name = "CONTINUOUSTRANSACTION_ID")
  private com.dimediary.model.entities.ContinuousTransactionEntity continuousTransaction;

  private Boolean fixCost;

  @javax.persistence.PrePersist
  private void setTimestamp() {
    this.timestamp = java.time.LocalDateTime.now();
  }

  public Double getAmount() {
    return AmountUtils.round(this.amount);
  }

  public void setAmount(final Double amount) {
    this.amount = AmountUtils.round(amount);
  }

  public void setTimestamp(final java.time.LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public Boolean getFixCost() {
    if (this.fixCost == null && this.category != null) {
      return this.category.getFixCost();
    }
    return this.fixCost;
  }

  public boolean equalsWithoutContinuousTransaction(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    final com.dimediary.model.entities.TransactionEntity other = (com.dimediary.model.entities.TransactionEntity) obj;
    if (this.amount == null) {
      if (other.amount != null) {
        return false;
      }
    } else if (!this.amount.equals(other.amount)) {
      return false;
    }
    if (this.bankAccount == null) {
      if (other.bankAccount != null) {
        return false;
      }
    } else if (!this.bankAccount.equals(other.bankAccount)) {
      return false;
    }
    if (this.category == null) {
      if (other.category != null) {
        return false;
      }
    } else if (!this.category.equals(other.category)) {
      return false;
    }
    if (this.date == null) {
      if (other.date != null) {
        return false;
      }
    } else if (!this.date.equals(other.date)) {
      return false;
    }
    if (this.fixCost == null) {
      if (other.fixCost != null) {
        return false;
      }
    } else if (!this.fixCost.equals(other.fixCost)) {
      return false;
    }
    if (this.id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!this.id.equals(other.id)) {
      return false;
    }
    if (this.name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!this.name.equals(other.name)) {
      return false;
    }
    if (this.timestamp == null) {
      if (other.timestamp != null) {
        return false;
      }
    } else if (!this.timestamp.equals(other.timestamp)) {
      return false;
    }

    return true;
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
