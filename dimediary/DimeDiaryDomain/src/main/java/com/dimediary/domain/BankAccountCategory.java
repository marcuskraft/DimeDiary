package com.dimediary.domain;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountCategory implements Serializable {
  
  private static final long serialVersionUID = 6790769181513794779L;
  private UUID id;
  private String name;
  private Boolean isRealAccount;

}
