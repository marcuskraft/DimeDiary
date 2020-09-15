package com.dimediary.model.entities;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import lombok.Data;

/**
 * class to persist account balances
 *
 * @author eyota
 */
@NamedQueries({
    @NamedQuery(name = BalanceEntity.ACCOUNT_BALANCE, query = "from BalanceHistoryEntity b WHERE b.bankAccount = :bankAccount"),
    @NamedQuery(name = BalanceEntity.ACCOUNT_BALANCE_DATE, query = "from BalanceHistoryEntity b WHERE b.bankAccount = :bankAccount AND b.date >= :date"),
    @NamedQuery(name = BalanceEntity.ACCOUNT_BALANCE_EXACT_DATE, query = "from BalanceHistoryEntity b WHERE b.bankAccount = :bankAccount AND b.date = :date"),
    @NamedQuery(name = BalanceEntity.LAST_ACCOUNT_BALANCE, query = "FROM BalanceHistoryEntity b1 WHERE b1.bankAccount = :bankAccount AND b1.date = (SELECT max(date) FROM BalanceHistoryEntity b2 WHERE b2.bankAccount = :bankAccount)"),
    @NamedQuery(name = BalanceEntity.LAST_ACCOUNT_BALANCE_BEFORE, query = "FROM BalanceHistoryEntity b WHERE b.bankAccount = :bankAccount AND b.date < :date ORDER BY b.date DESC"),
    @NamedQuery(name = BalanceEntity.DELETE_ALL_BALANCE_HISTORIES, query = "DELETE FROM BalanceEntity")})
@Entity
@Table(name = "balances")
@IdClass(BalanceHistoryPrimaryKey.class)
@Data
public class BalanceEntity implements Serializable {

  public static final String ACCOUNT_BALANCE_DATE = "accountBalanceDate";
  public static final String DELETE_ALL_BALANCE_HISTORIES = "DELETE_ALL_BALANCE_HISTORIES";
  public static final String LAST_ACCOUNT_BALANCE_BEFORE = "LAST_ACCOUNT_BALANCE_BEFORE";
  public static final String ACCOUNT_BALANCE_EXACT_DATE = "ACCOUNT_BALANCE_AT_EXACT_DATE";
  public static final String ACCOUNT_BALANCE = "accountBalance";
  public static final String LAST_ACCOUNT_BALANCE = "lastAccountBalance";

  /**
   *
   */
  private static final long serialVersionUID = -8067932338253105032L;


  @Id
  private LocalDate date;

  @Id
  @ManyToOne
  @JoinColumn(name = "bank_account_id")
  private BankAccountEntity bankAccount;
  
  private Integer balanceEuroCent;


}
