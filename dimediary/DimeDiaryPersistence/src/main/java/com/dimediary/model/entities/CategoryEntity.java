package com.dimediary.model.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;


@Entity
@Table(name = "category")
@Data
public class CategoryEntity implements Serializable {

  private static final long serialVersionUID = -8922036259983542096L;

  @Id
  private String id;
  private String name;
  private Boolean fixCost;

}
