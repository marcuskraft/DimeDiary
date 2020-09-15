package com.dimediary.model.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import lombok.Data;

/**
 * entity class for bank accounts
 *
 * @author eyota
 */
@NamedQueries({
    @NamedQuery(name = BankAccountEntity.ALL_BANK_ACCOUNTS, query = "from BankAccountEntity"),
    @NamedQuery(name = BankAccountEntity.FIND_BANKACCOUNTS_WITH_CATEGORY, query = "from BankAccountEntity b WHERE bankAccountCategory = :bankAccountCategory"),
    @NamedQuery(name = BankAccountEntity.FIND_BANK_ACCOUNTS, query = "from BankAccountEntity b WHERE b.name IN :namesList"),
    @NamedQuery(name = BankAccountEntity.DELETE_ALL_BANKACCOUNT, query = "DELETE FROM BankAccountEntity")})
@Entity
@Table(name = "bank_account")
@Data
public class BankAccountEntity implements Serializable {

  public static final String DELETE_ALL_BANKACCOUNT = "deleteAllBankaccount";
  public static final String FIND_BANK_ACCOUNTS = "findBankAccounts";
  public static final String FIND_BANKACCOUNTS_WITH_CATEGORY = "findBankaccountsWithCategory";
  public static final String ALL_BANK_ACCOUNTS = "allBankAccounts";


  /**
   *
   */
  private static final long serialVersionUID = 7788504506056118005L;

  @Id
  private UUID id;
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
