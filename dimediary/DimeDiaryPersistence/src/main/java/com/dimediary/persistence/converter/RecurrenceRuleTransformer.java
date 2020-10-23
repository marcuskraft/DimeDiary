package com.dimediary.persistence.converter;

import org.dmfs.rfc5545.recur.InvalidRecurrenceRuleException;
import org.dmfs.rfc5545.recur.RecurrenceRule;
import org.dmfs.rfc5545.recur.RecurrenceRule.RfcMode;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecurrenceRuleTransformer {


  static RecurrenceRule to(final String recurrenceRuleString) {
    if (recurrenceRuleString == null) {
      return null;
    }
    try {
      return new RecurrenceRule(recurrenceRuleString, RfcMode.RFC5545_STRICT);
    } catch (final InvalidRecurrenceRuleException e) {
      throw new IllegalStateException(
          "RuleString is no valid recurrence rule: " + recurrenceRuleString, e);
    }
  }

  static String from(final RecurrenceRule recurrenceRule) {
    if (recurrenceRule == null) {
      return null;
    }
    return recurrenceRule.toString();
  }

}
