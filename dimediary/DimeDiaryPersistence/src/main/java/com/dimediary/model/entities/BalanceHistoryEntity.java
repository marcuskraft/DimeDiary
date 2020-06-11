package com.dimediary.model.entities;

import com.dimediary.domain.helper.AmountUtils;

/**
 * class to persist account balances
 *
 * @author eyota
 */
@javax.persistence.NamedQueries({
    @javax.persistence.NamedQuery(name = "accountBalance", query = "from BalanceHistoryEntity b WHERE b.bankAccount = :bankAccount"),
    @javax.persistence.NamedQuery(name = com.dimediary.model.entities.BalanceHistoryEntity.ACCOUNT_BALANCE_DATE, query = "from BalanceHistoryEntity b WHERE b.bankAccount = :bankAccount AND b.date >= :date"),
    @javax.persistence.NamedQuery(name = com.dimediary.model.entities.BalanceHistoryEntity.ACCOUNT_BALANCE_EXACT_DATE, query = "from BalanceHistoryEntity b WHERE b.bankAccount = :bankAccount AND b.date = :date"),
    @javax.persistence.NamedQuery(name = "lastAccountBalance", query = "FROM BalanceHistoryEntity b1 WHERE b1.bankAccount = :bankAccount AND b1.date = (SELECT max(date) FROM BalanceHistoryEntity b2 WHERE b2.bankAccount = :bankAccount)"),
    @javax.persistence.NamedQuery(name = com.dimediary.model.entities.BalanceHistoryEntity.LAST_ACCOUNT_BALANCE_BEFORE, query = "FROM BalanceHistoryEntity b WHERE b.bankAccount = :bankAccount AND b.date < :date ORDER BY b.date DESC"),
    @javax.persistence.NamedQuery(name = com.dimediary.model.entities.BalanceHistoryEntity.DELETE_ALL_BALANCE_HISTORIES, query = "DELETE FROM BalanceHistoryEntity")})
@javax.persistence.Entity
@javax.persistence.Table(name = "BALANCE_HISTORY")
@javax.persistence.IdClass(com.dimediary.model.entities.BalanceHistoryPK.class)
@lombok.Data
public class BalanceHistoryEntity implements java.io.Serializable {

  public static final String ACCOUNT_BALANCE_DATE = "accountBalanceDate";
  public static final String DELETE_ALL_BALANCE_HISTORIES = "DELETE_ALL_BALANCE_HISTORIES";
  public static final String LAST_ACCOUNT_BALANCE_BEFORE = "LAST_ACCOUNT_BALANCE_BEFORE";
  public static final String ACCOUNT_BALANCE_EXACT_DATE = "ACCOUNT_BALANCE_AT_EXACT_DATE";

  /**
   *
   */
  private static final long serialVersionUID = -8067932338253105032L;

  @javax.persistence.Id
  @javax.persistence.Column(name = "DATE")
  private java.time.LocalDate date;

  @javax.persistence.Id
  @javax.persistence.ManyToOne
  @javax.persistence.JoinColumn(name = "BANKACCOUNT_NAME")
  private com.dimediary.model.entities.BankAccountEntity bankAccount;

  @javax.persistence.Column(name = "AMOUNT")
  private Double amount;

  /**
   * @return amount of this balance
   */
  public Double getAmount() {
    return AmountUtils.round(this.amount);
  }

  /**
   * @param amount amount of this balance
   */
  public void setAmount(final Double amount) {
    this.amount = AmountUtils.round(amount);
  }

  /**
   * adds an amount to this balance (e.g. if a transaction was added or deleted)
   *
   * @param amount amount to be added (can be negative)
   */
  public void addAmount(final Double amount) {
    this.amount = AmountUtils.round(this.amount + amount);
  }

}
