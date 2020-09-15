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
 * entity class for the categories
 *
 * @author eyota
 */
@NamedQueries({
    @NamedQuery(name = "allCategories", query = "from CategoryEntity"),
    @NamedQuery(name = "findCategories", query = "from CategoryEntity c WHERE c.name IN :namesList"),
    @NamedQuery(name = CategoryEntity.DELETE_ALL_CATEGORIES, query = "DELETE FROM CategoryEntity")})
@Entity
@Table(name = "category")
@Data
public class CategoryEntity implements Serializable {

  public static final String DELETE_ALL_CATEGORIES = "DELETE_ALL_CATEGORIES";

  /**
   *
   */
  private static final long serialVersionUID = -8922036259983542096L;

  @Id
  private UUID id;
  private String name;
  private Boolean fixCost;

}
