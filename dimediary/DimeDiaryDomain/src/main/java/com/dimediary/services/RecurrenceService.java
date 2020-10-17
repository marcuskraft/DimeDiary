package com.dimediary.services;

import static org.dmfs.rfc5545.recur.RecurrenceRule.RfcMode.RFC5545_STRICT;

import com.dimediary.domain.RecurrenceSettings;
import com.dimediary.domain.RecurrenceSettings.RecurrenceType;
import com.dimediary.utils.date.DateUtils;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.dmfs.rfc5545.Weekday;
import org.dmfs.rfc5545.recur.Freq;
import org.dmfs.rfc5545.recur.InvalidRecurrenceRuleException;
import org.dmfs.rfc5545.recur.RecurrenceRule;
import org.dmfs.rfc5545.recur.RecurrenceRule.Part;
import org.dmfs.rfc5545.recur.RecurrenceRule.WeekdayNum;
import org.springframework.stereotype.Service;

@Service
public class RecurrenceService {

  public RecurrenceRule getRecurrenceRule(final RecurrenceSettings recurrenceSettings)
      throws InvalidRecurrenceRuleException {

    final RecurrenceRule recurrenceRule;
    final Freq freq = Freq.MONTHLY;
    switch (recurrenceSettings.getRecurrenceType()) {
      case DAILY:
        recurrenceRule = new RecurrenceRule(Freq.DAILY, RFC5545_STRICT);
        recurrenceRule
            .setByDayPart(this.dayOfWeeksToWeekDayNums(recurrenceSettings.getDayOfWeeks()));
        break;
      case YEARLY:
        recurrenceRule = new RecurrenceRule(Freq.YEARLY, RFC5545_STRICT);
        this.setBayPartMonthDay(recurrenceSettings, recurrenceRule);
        break;
      case WEEKLY:
        recurrenceRule = new RecurrenceRule(Freq.WEEKLY, RFC5545_STRICT);
        recurrenceRule
            .setByDayPart(this.dayOfWeeksToWeekDayNums(recurrenceSettings.getDayOfWeeks()));
        break;
      case MONTHLY:
      default:
        recurrenceRule = new RecurrenceRule(Freq.MONTHLY, RFC5545_STRICT);
        this.setBayPartMonthDay(recurrenceSettings, recurrenceRule);
        break;
    }

    if (recurrenceSettings.getInterval() != null) {
      recurrenceRule.setInterval(recurrenceSettings.getInterval());
    }

    if (recurrenceSettings.getUntil() != null) {
      recurrenceRule.setUntil(DateUtils.localDateToDateTime(recurrenceSettings.getUntil()));
    } else if (recurrenceSettings.getCount() != null) {
      recurrenceRule.setCount(recurrenceSettings.getCount());
    }

    return recurrenceRule;
  }

  public RecurrenceSettings getRecurrenceSettings(final RecurrenceRule recurrenceRule,
      final LocalDate beginDate, final Collection<LocalDate> recurrenceExceptions,
      final Collection<LocalDate> extraInstances) {
    final RecurrenceSettings recurrenceSettings = new RecurrenceSettings();

    recurrenceSettings.setRecurrenceExceptions(recurrenceExceptions);
    recurrenceSettings.setRecurrenceExtraInstances(extraInstances);

    recurrenceSettings.setRecurrenceType(this.getRecurrenceType(recurrenceRule.getFreq()));

    recurrenceSettings.setBeginDate(beginDate);

    if (recurrenceRule.getCount() != null) {
      recurrenceSettings.setCount(recurrenceRule.getCount());
    } else if (recurrenceRule.getUntil() != null) {
      recurrenceSettings.setUntil(DateUtils.dateTimeToLocalDate(recurrenceRule.getUntil()));
    }

    final List<Integer> monthDays = recurrenceRule.getByPart(Part.BYMONTHDAY);
    if (monthDays.size() == 1) {
      Integer dayOfMonth = monthDays.get(0);
      if (dayOfMonth < 0) {
        recurrenceSettings.setDayOfMonthFromBehind(true);
        dayOfMonth = Math.abs(dayOfMonth);
      } else {
        recurrenceSettings.setDayOfMonthFromBehind(false);
      }
      recurrenceSettings.setDayOfMonth(dayOfMonth);
    } else if (monthDays.size() > 1) {
      throw new IllegalStateException("There should only be one month day in a recurrence rule");
    }

    if (recurrenceRule.getByDayPart().size() > 0) {
      recurrenceSettings.setDayOfWeeks(this.weekDayNumsToDayOfWeeks(recurrenceRule.getByDayPart()));
    }

    recurrenceSettings.setInterval(recurrenceRule.getInterval());

    return recurrenceSettings;
  }

  private Collection<DayOfWeek> weekDayNumsToDayOfWeeks(final List<WeekdayNum> byDayPart) {
    return byDayPart.stream().map(weekdayNum -> this.weekDayNumToDayOfWeek(weekdayNum.weekday))
        .collect(
            Collectors.toList());
  }

  private RecurrenceType getRecurrenceType(final Freq freq) {
    switch (freq) {
      case YEARLY:
        return RecurrenceType.YEARLY;
      case MONTHLY:
        return RecurrenceType.MONTHLY;
      case WEEKLY:
        return RecurrenceType.WEEKLY;
      case DAILY:
        return RecurrenceType.DAILY;
      case HOURLY:
      case MINUTELY:
      case SECONDLY:
      default:
        throw new IllegalArgumentException("The frequency" + freq.name() + "is not supported!");
    }
  }

  private void setBayPartMonthDay(final RecurrenceSettings recurrenceSettings,
      final RecurrenceRule recurrenceRule) throws InvalidRecurrenceRuleException {
    if (recurrenceSettings.getDayOfMonth() != null) {
      final Integer dayOfMonth = recurrenceSettings.getDayOfMonth();
      recurrenceRule.setByPart(Part.BYMONTHDAY,
          recurrenceSettings.isDayOfMonthFromBehind() ? -dayOfMonth : dayOfMonth);
    }
  }

  private List<WeekdayNum> dayOfWeeksToWeekDayNums(final Collection<DayOfWeek> dayOfWeeks) {
    return dayOfWeeks.stream()
        .map(dayOfWeek -> new WeekdayNum(0, this.dayOfWeekToWeekDay(dayOfWeek)))
        .collect(
            Collectors.toList());
  }

  private Weekday dayOfWeekToWeekDay(final DayOfWeek dayOfWeek) {
    switch (dayOfWeek) {
      case MONDAY:
        return Weekday.MO;
      case TUESDAY:
        return Weekday.TU;
      case WEDNESDAY:
        return Weekday.WE;
      case THURSDAY:
        return Weekday.TH;
      case FRIDAY:
        return Weekday.FR;
      case SATURDAY:
        return Weekday.SA;
      case SUNDAY:
      default:
        return Weekday.SU;
    }
  }

  private DayOfWeek weekDayNumToDayOfWeek(final Weekday weekday) {
    switch (weekday) {
      case MO:
        return DayOfWeek.MONDAY;
      case TU:
        return DayOfWeek.TUESDAY;
      case WE:
        return DayOfWeek.WEDNESDAY;
      case TH:
        return DayOfWeek.THURSDAY;
      case FR:
        return DayOfWeek.FRIDAY;
      case SA:
        return DayOfWeek.SATURDAY;
      case SU:
      default:
        return DayOfWeek.SUNDAY;
    }
  }

}
