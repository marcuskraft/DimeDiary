package com.dimediary.model.entities;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "recurrence_extra_instance")
@Data
public class RecurrenceExtraInstanceEntity {

  @Id
  private String id;

  @ManyToOne
  @JoinColumn
  private ContinuousTransactionEntity continuousTransaction;
  
  private LocalDate instanceDate;

}
