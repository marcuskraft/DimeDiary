package com.dimediary.persistence.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "bank_account_category")
@Data
public class BankAccountCategoryEntity implements Serializable {


  private static final long serialVersionUID = -6934027507205045824L;


  @Id
  private String id;
  private String name;
  private Boolean isRealAccount;

}
