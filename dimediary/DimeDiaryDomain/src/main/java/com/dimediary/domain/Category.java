package com.dimediary.domain;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 4930547606652766922L;
  private String name;
  private Boolean fixCost;

}
