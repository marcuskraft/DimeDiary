package com.dimediary.utils.recurrence;

import com.dimediary.utils.date.DateUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dmfs.rfc5545.DateTime;
import org.dmfs.rfc5545.recur.InvalidRecurrenceRuleException;
import org.dmfs.rfc5545.recur.RecurrenceRule;
import org.dmfs.rfc5545.recur.RecurrenceRule.RfcMode;
import org.dmfs.rfc5545.recur.RecurrenceRuleIterator;

public class RecurrenceRuleUtils {

  // TODO: write junit tests
  private final static Logger log = LogManager.getLogger(RecurrenceRuleUtils.class);


  public static List<LocalDate> getDatesForRecurrenceRule(final RecurrenceRule recurrenceRule,
      final DateTime beginOfRule, final DateTime dateFrom) {

    final List<LocalDate> dates = new ArrayList<>();
    final RecurrenceRuleIterator recurrenceRuleIterator = recurrenceRule.iterator(beginOfRule);

    if (dateFrom != null) {
      recurrenceRuleIterator.fastForward(dateFrom);
    }

    final LocalDate dateUntilMax = LocalDate.now()
        .plusMonths(DateUtils.numberOfMonthFutureTransactions);

    LocalDate date = DateUtils.dateTimeToLocalDate(beginOfRule);
    while (recurrenceRuleIterator.hasNext() && date.isBefore(dateUntilMax)) {
      final DateTime dateTime = recurrenceRuleIterator.nextDateTime();
      date = DateUtils.dateTimeToLocalDate(dateTime);
      dates.add(date);
    }
    return dates;
  }

  public static List<LocalDate> getDatesForRecurrenceRule(final RecurrenceRule recurrenceRule,
      final LocalDate beginOfRule, final LocalDate dateFrom) {
    return RecurrenceRuleUtils
        .getDatesForRecurrenceRule(recurrenceRule, DateUtils.localDateToDateTime(beginOfRule),
            DateUtils.localDateToDateTime(dateFrom));
  }

  public static RecurrenceRule createRecurrenceRule(final String ruleString) {
    try {
      return new RecurrenceRule(ruleString, RfcMode.RFC5545_STRICT);
    } catch (final InvalidRecurrenceRuleException e) {
      RecurrenceRuleUtils.log.error("RuleString ist keine g�ltige RecurrenceRule", e);
      throw new IllegalStateException("RuleString ist keine g�ltige RecurrenceRule", e);
    }
  }

  public static LocalDate getLastDateByCount(final RecurrenceRule recurrenceRule,
      final DateTime begin) {
    if (recurrenceRule.isInfinite()) {
      return null;
    }

    final RecurrenceRuleIterator recurrenceRuleIterator = recurrenceRule.iterator(begin);
    recurrenceRuleIterator.skipAllButLast();
    LocalDate date = null;
    if (recurrenceRuleIterator.hasNext()) {
      date = DateUtils.dateTimeToLocalDate(recurrenceRuleIterator.nextDateTime());
    }
    return date;
  }

  public static LocalDate getLastRecurrenceDateBefore(final RecurrenceRule recurrenceRule,
      final LocalDate beginOfRule, final LocalDate date) {
    final List<LocalDate> localDates = RecurrenceRuleUtils
        .getDatesForRecurrenceRule(recurrenceRule, beginOfRule,
            null);

    return DateUtils.getLastDateBefore(date, localDates);

  }

  public static LocalDate getFirstRecurrenceDateAfter(final RecurrenceRule recurrenceRule,
      final LocalDate beginOfRule, final LocalDate date) {
    final List<LocalDate> localDates = RecurrenceRuleUtils
        .getDatesForRecurrenceRule(recurrenceRule, beginOfRule,
            date);

    return DateUtils.getFirstDateAfter(date, localDates);

  }

}
