package com.dimediary.domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecurrenceSettings {

  public enum RecurrenceType {
    DAILY, WEEKLY, MONTHLY, YEARLY
  }

  private Collection<LocalDate> recurrenceExceptions;
  private Collection<LocalDate> recurrenceExtraInstances;

  private RecurrenceType recurrenceType;
  private Integer interval;
  private Integer dayOfMonth;
  private boolean isDayOfMonthFromBehind = false;
  private Collection<DayOfWeek> dayOfWeeks;

  private boolean isInfinite = true;
  private LocalDate beginDate;
  private LocalDate until;
  private Integer count;


}
