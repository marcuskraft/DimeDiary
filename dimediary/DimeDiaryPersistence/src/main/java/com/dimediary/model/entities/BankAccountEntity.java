package com.dimediary.model.entities;

/**
 * entity class for bank accounts
 *
 * @author eyota
 */
@javax.persistence.NamedQueries({
    @javax.persistence.NamedQuery(name = com.dimediary.model.entities.BankAccountEntity.ALL_BANK_ACCOUNTS, query = "from BankAccountEntity"),
    @javax.persistence.NamedQuery(name = com.dimediary.model.entities.BankAccountEntity.FIND_BANKACCOUNTS_WITH_CATEGORY, query = "from BankAccountEntity b WHERE bankAccountCategory = :bankAccountCategory"),
    @javax.persistence.NamedQuery(name = com.dimediary.model.entities.BankAccountEntity.FIND_BANK_ACCOUNTS, query = "from BankAccountEntity b WHERE b.name IN :namesList"),
    @javax.persistence.NamedQuery(name = com.dimediary.model.entities.BankAccountEntity.DELETE_ALL_BANKACCOUNT, query = "DELETE FROM BankAccountEntity")})
@javax.persistence.Entity
@javax.persistence.Table(name = "BANKACCOUNT")
@lombok.Data
public class BankAccountEntity implements java.io.Serializable {

  public static final String DELETE_ALL_BANKACCOUNT = "deleteAllBankaccount";

  public static final String FIND_BANK_ACCOUNTS = "findBankAccounts";

  public static final String FIND_BANKACCOUNTS_WITH_CATEGORY = "findBankaccountsWithCategory";

  public static final String ALL_BANK_ACCOUNTS = "allBankAccounts";

  /**
   *
   */
  private static final long serialVersionUID = 7788504506056118005L;

  @javax.persistence.Id
  @javax.persistence.Column(name = "NAME")
  private String name;

  @javax.persistence.Column(name = "BANKNAME")
  private String bankName;

  @javax.persistence.Column(name = "IBAN")
  private String iban;

  @javax.persistence.Column(name = "BIC")
  private String bic;

  @javax.persistence.ManyToOne
  @javax.persistence.JoinColumn(name = "BANKACCOUNT_CATEGORY_NAME")
  private com.dimediary.model.entities.BankAccountCategoryEntity bankAccountCategory;

  @javax.persistence.Column(name = "DATE_STARTBUDGET")
  private java.time.LocalDate dateStartBudget;

  @javax.persistence.Column(name = "STARTBUDGET")
  private Double startBudget;

}
