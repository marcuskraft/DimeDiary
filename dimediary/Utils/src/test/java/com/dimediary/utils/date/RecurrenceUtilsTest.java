package com.dimediary.utils.date;

import com.dimediary.utils.recurrence.RecurrenceRuleUtils;
import org.dmfs.rfc5545.recur.RecurrenceRule;
import org.dmfs.rfc5545.recurrenceset.RecurrenceRuleAdapter;
import org.dmfs.rfc5545.recurrenceset.RecurrenceSet;
import org.junit.jupiter.api.Test;

public class RecurrenceUtilsTest {

  @Test
  void testRecurrenceSet() {
    final RecurrenceSet recurrenceSet = new RecurrenceSet();

    final String ruleString = "FREQ=MONTHLY;BYMONTHDAY=1;UNTIL=20191031T230000Z";
    final RecurrenceRule recurrenceRule = RecurrenceRuleUtils
        .createRecurrenceRule(ruleString);

    recurrenceSet.addInstances(new RecurrenceRuleAdapter(recurrenceRule));

    final String testString = recurrenceSet.

        Assertions.assertEquals(ruleString, testString);

  }

}
