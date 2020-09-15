package com.dimediary.model.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.Data;

@NamedQueries({
    @NamedQuery(name = TransactionEntity.TRANSACTIONS_BETWEEN, query =
        "from TransactionEntity t WHERE t.bankAccount = :bankAccount"
            + " AND t.date BETWEEN :dateFrom AND :dateUntil ORDER BY t.date"),
    @NamedQuery(name = TransactionEntity.ALL_ACCOUNT_TRANSACTIONS, query = "from TransactionEntity t WHERE t.bankAccount = :bankAccount"),
    @NamedQuery(name = TransactionEntity.TRANSACTIONS_AT_DAY, query = "from TransactionEntity t WHERE t.bankAccount = :bankAccount AND t.date = :date order by t.timestamp"),
    @NamedQuery(name = TransactionEntity.CONTINUOUS_TRANSANSACTION_FROM_DATE, query =
        "from TransactionEntity t WHERE t.continuousTransaction = :continuousTransaction "
            + "AND t.date >= :date"),
    @NamedQuery(name = TransactionEntity.CONTINUOUS_TRANSANSACTION_UNTIL_DATE, query =
        "from TransactionEntity t WHERE t.continuousTransaction = :continuousTransaction "
            + "AND t.date <= :date"),
    @NamedQuery(name = TransactionEntity.CONTINUOUS_TRANSACTIONS, query = "from TransactionEntity t WHERE t.continuousTransaction = :continuousTransaction"),
    @NamedQuery(name = TransactionEntity.TRANSACTIONS_WITHOUT_ACCOUNT_BETWEEN, query =
        "from TransactionEntity t where t.bankAccount is null and t.date BETWEEN :dateFrom"
            + " AND :dateUntil ORDER BY t.date"),
    @NamedQuery(name = TransactionEntity.TRANSACTIONS_WITHOUT_ACCOUNT, query = "from TransactionEntity t where t.bankAccount is null and t.date = :date"),
    @NamedQuery(name = TransactionEntity.DELETE_ALL_TRANSACTIONS, query = "DELETE FROM TransactionEntity")})
@Entity
@Table(name = "TRANSACTIONS", indexes = {
    @Index(columnList = "date", name = "transaction_date_hidx")})
@Data
public class TransactionEntity implements Serializable {

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

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;


  private String name;
  private Integer amount;

  @ManyToOne
  @JoinColumn(name = "bank_account_id")
  private com.dimediary.model.entities.BankAccountEntity bankAccount;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private CategoryEntity category;

  private LocalDate date;

  private java.time.LocalDateTime timestamp;

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
