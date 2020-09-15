package com.dimediary.model.entities;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import lombok.Data;

/**
 * entity for the bank account categories
 *
 * @author eyota
 */
@NamedQueries({
    @NamedQuery(name = BankAccountCategoryEntity.ALL_ACCOUNT_CATEGORIES, query = "from BankAccountCategoryEntity"),
    @NamedQuery(name = "findAccountCategories", query = "from BankAccountCategoryEntity b WHERE b.name IN :nameList"),
    @NamedQuery(name = BankAccountCategoryEntity.DELETE_ALL_BANKACCOUNT_CATEGORIES, query = "DELETE FROM BankAccountCategoryEntity")})
@Entity
@Table(name = "bank_account_category")
@Data
public class BankAccountCategoryEntity implements Serializable {

  public static final String DELETE_ALL_BANKACCOUNT_CATEGORIES = "DELETE_ALL_BANKACCOUNT_CATEGORIES";
  public static final String ALL_ACCOUNT_CATEGORIES = "allAccountCategories";

  private static final long serialVersionUID = -6934027507205045824L;


  @Id
  private UUID id;
  private String name;
  private Boolean isRealAccount;

}
