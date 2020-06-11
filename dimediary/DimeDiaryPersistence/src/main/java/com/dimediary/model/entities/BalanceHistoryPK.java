package com.dimediary.model.entities;

/**
 * primary key class for BalanceHistory
 *
 * @author eyota
 */
public class BalanceHistoryPK implements java.io.Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -4539881778212051537L;
  private java.time.LocalDate date;
  private com.dimediary.model.entities.BankAccountEntity bankAccount;

  /**
   * constructor
   */
  public BalanceHistoryPK() {
    super();
  }

  /**
   * constructor
   *
   * @param bankAccount bank account for which this balance key is for
   * @param date        Date for which this balance history is for
   */
  public BalanceHistoryPK(final com.dimediary.model.entities.BankAccountEntity bankAccount,
      final java.time.LocalDate date) {
    super();
    this.date = date;
    this.bankAccount = bankAccount;
  }

  /**
   * @return Date for this balance history
   */
  public java.time.LocalDate getDate() {
    return this.date;
  }

  /**
   * @param date Date for this balance history
   */
  public void setDate(final java.time.LocalDate date) {
    this.date = date;
  }

  /**
   * @return bank account for which this balance key is for
   */
  public com.dimediary.model.entities.BankAccountEntity getBankAccount() {
    return this.bankAccount;
  }

  /**
   * @param bankAccount bank account for which this balance key is for
   */
  public void setBankAccount(final com.dimediary.model.entities.BankAccountEntity bankAccount) {
    this.bankAccount = bankAccount;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (this.bankAccount == null ? 0 : this.bankAccount.hashCode());
    result = prime * result + (this.date == null ? 0 : this.date.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    final com.dimediary.model.entities.BalanceHistoryPK other = (com.dimediary.model.entities.BalanceHistoryPK) obj;
    if (this.bankAccount == null) {
      if (other.bankAccount != null) {
        return false;
      }
    } else if (!this.bankAccount.equals(other.bankAccount)) {
      return false;
    }
    if (this.date == null) {
      if (other.date != null) {
        return false;
      }
    } else if (!this.date.equals(other.date)) {
      return false;
    }
    return true;
  }

}
