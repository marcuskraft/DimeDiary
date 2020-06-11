package com.dimediary.model.entities;

/**
 * entity class for the categories
 *
 * @author eyota
 */
@javax.persistence.NamedQueries({
    @javax.persistence.NamedQuery(name = "allCategories", query = "from CategoryEntity"),
    @javax.persistence.NamedQuery(name = "findCategories", query = "from CategoryEntity c WHERE c.name IN :namesList"),
    @javax.persistence.NamedQuery(name = com.dimediary.model.entities.CategoryEntity.DELETE_ALL_CATEGORIES, query = "DELETE FROM CategoryEntity")})
@javax.persistence.Entity
@javax.persistence.Table(name = "CATEGORY")
@lombok.Data
public class CategoryEntity implements java.io.Serializable {

  public static final String DELETE_ALL_CATEGORIES = "DELETE_ALL_CATEGORIES";

  /**
   *
   */
  private static final long serialVersionUID = -8922036259983542096L;

  @javax.persistence.Id
  @javax.persistence.Column(name = "NAME")
  private String name;

  private Boolean fixCost;

}
