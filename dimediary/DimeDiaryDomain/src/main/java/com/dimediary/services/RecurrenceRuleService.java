package com.dimediary.services;

import com.dimediary.utils.date.DateUtils;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;
import org.dmfs.rfc5545.recur.RecurrenceRule;
import org.dmfs.rfc5545.recurrenceset.RecurrenceList;
import org.dmfs.rfc5545.recurrenceset.RecurrenceRuleAdapter;
import org.dmfs.rfc5545.recurrenceset.RecurrenceSet;
import org.dmfs.rfc5545.recurrenceset.RecurrenceSetIterator;
import org.springframework.stereotype.Service;

@Service
public class RecurrenceRuleService {


  public static final LocalDate DATE_UNTIL_MAX = LocalDate.now()
      .plusMonths(DateUtils.numberOfMonthFutureTransactions);

  public List<LocalDate> getDatesForRecurrenceSettings(final RecurrenceRule recurrenceRule,
      final LocalDate beginDate, final Collection<LocalDate> exceptions,
      final Collection<LocalDate> extraInstances) {
    return this.getDatesForRecurrenceSettings(recurrenceRule, beginDate, exceptions, extraInstances,
        DATE_UNTIL_MAX);
  }


  public List<LocalDate> getDatesForRecurrenceSettings(final RecurrenceRule recurrenceRule,
      final LocalDate beginDate, final Collection<LocalDate> exceptions,
      final Collection<LocalDate> extraInstances, final LocalDate dateUntilMax) {
    final RecurrenceSet recurrenceSet = new RecurrenceSet();

    recurrenceSet.addInstances(new RecurrenceRuleAdapter(recurrenceRule));

    if (extraInstances != null) {
      recurrenceSet
          .addInstances(new RecurrenceList(extraInstances.stream()
              .map(DateUtils::getMillis).mapToLong(value -> value).toArray()));
    }

    if (exceptions != null) {
      recurrenceSet
          .addExceptions(new RecurrenceList(exceptions.stream()
              .map(DateUtils::getMillis).mapToLong(value -> value).toArray()));
    }

    final RecurrenceSetIterator recurrenceRuleIterator = recurrenceSet
        .iterator(TimeZone.getTimeZone(
            ZoneId.of("Europe/Berlin")), DateUtils.getMillis(beginDate));

    final List<LocalDate> dates = new ArrayList<>();

    LocalDate date = beginDate;
    while (recurrenceRuleIterator.hasNext() && date.isBefore(dateUntilMax)) {
      date = DateUtils.fromMillis(recurrenceRuleIterator.next());
      dates.add(date);
    }

    return dates;

  }


}
