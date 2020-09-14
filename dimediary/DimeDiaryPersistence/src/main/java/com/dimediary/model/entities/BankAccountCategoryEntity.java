package com.dimediary.model.entities;

/**
 * entity for the bank account categories
 *
 * @author eyota
 */
@javax.persistence.NamedQueries({
    @javax.persistence.NamedQuery(name = BankAccountCategoryEntity.ALL_ACCOUNT_CATEGORIES, query = "from BankAccountCategoryEntity"),
    @javax.persistence.NamedQuery(name = "findAccountCategories", query = "from BankAccountCategoryEntity b WHERE b.name IN :nameList"),
    @javax.persistence.NamedQuery(name = com.dimediary.model.entities.BankAccountCategoryEntity.DELETE_ALL_BANKACCOUNT_CATEGORIES, query = "DELETE FROM BankAccountCategoryEntity")})
@javax.persistence.Entity
@javax.persistence.Table(name = "BANKACCOUNT_CATEGORY")
@lombok.Data
public class BankAccountCategoryEntity implements java.io.Serializable {

  public static final String DELETE_ALL_BANKACCOUNT_CATEGORIES = "DELETE_ALL_BANKACCOUNT_CATEGORIES";
  public static final String ALL_ACCOUNT_CATEGORIES = "allAccountCategories";

  /**
   *
   */
  private static final long serialVersionUID = -6934027507205045824L;


  @javax.persistence.Id
  @javax.persistence.Column(name = "NAME")
  private String name;

  @javax.persistence.Column(name = "IS_REAL_ACCOUNT")
  private Boolean isRealAccount;

}
