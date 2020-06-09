package com.dimediary.utils.date;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.dmfs.rfc5545.DateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DateUtilsTest {

  private static final LocalDate UNTIL_DATE = LocalDate.of(2019, 2, 5);
  private static final LocalDate FROM_DATE = LocalDate.of(2018, 4, 12);

  @Test
  public void localDateTimeToDateTimeTest() {
    final LocalDate localDateBefore = LocalDate.now();
    final DateTime dateTime = DateUtils.localDateToDateTime(localDateBefore);
    final LocalDate localDateAfter = DateUtils.dateTimeToLocalDate(dateTime);

    Assertions.assertTrue(localDateBefore.compareTo(localDateAfter) == 0,
        "localDateToDateTime and dateTimeToLocalDate not reversible methods");

  }

  @Test
  public void localDateToDateTimeTest() {
    final LocalDate localDate = LocalDate.of(2018, 2, 28);
    final DateTime dateTime = DateUtils.localDateToDateTime(localDate);

    Assertions.assertTrue(localDate.getYear() == dateTime.getYear());
    Assertions.assertTrue(localDate.getMonthValue() == dateTime.getMonth() + 1);
    Assertions.assertTrue(localDate.getDayOfMonth() == dateTime.getDayOfMonth());
  }

  @Test
  public void dateTimeToLocalDateTest() {
    final DateTime dateTime = DateTime.now();
    final LocalDate localDate = DateUtils.dateTimeToLocalDate(dateTime);

    Assertions.assertTrue(localDate.getYear() == dateTime.getYear());
    Assertions.assertTrue(localDate.getMonthValue() == dateTime.getMonth() + 1);
    Assertions.assertTrue(localDate.getDayOfMonth() == dateTime.getDayOfMonth());
  }

  @Test
  public void getLastSundayTest() {
    final LocalDate localDate = LocalDate.of(2018, 4, 30);
    final LocalDate lastSunday = DateUtils.getLastSunday(localDate);

    Assertions.assertTrue(lastSunday.isBefore(localDate));
    Assertions.assertTrue(lastSunday.getDayOfWeek() == DayOfWeek.SUNDAY);
    Assertions.assertTrue(2018 == lastSunday.getYear());
    Assertions.assertTrue(4 == lastSunday.getMonthValue());
    Assertions.assertTrue(29 == lastSunday.getDayOfMonth());

    final LocalDate lastSunday2 = DateUtils.getLastSunday(lastSunday);
    Assertions.assertTrue(lastSunday2.equals(lastSunday));

    final LocalDate lastSunday3 = DateUtils.getLastSundayAlways(lastSunday);

    Assertions.assertTrue(lastSunday3.isBefore(lastSunday));
    Assertions.assertTrue(lastSunday3.getDayOfWeek() == DayOfWeek.SUNDAY);
    Assertions.assertTrue(2018 == lastSunday3.getYear());
    Assertions.assertTrue(4 == lastSunday3.getMonthValue());
    Assertions.assertTrue(22 == lastSunday3.getDayOfMonth());
  }

  @Test
  public void getNextSundayTest() {
    final LocalDate localDate = LocalDate.of(2018, 4, 30);
    final LocalDate nextSunday = DateUtils.getNextSunday(localDate);

    Assertions.assertTrue(nextSunday.isAfter(localDate));
    Assertions.assertTrue(nextSunday.getDayOfWeek() == DayOfWeek.SUNDAY);
    Assertions.assertTrue(2018 == nextSunday.getYear());
    Assertions.assertTrue(5 == nextSunday.getMonthValue());
    Assertions.assertTrue(6 == nextSunday.getDayOfMonth());

    final LocalDate nextSunday2 = DateUtils.getNextSunday(nextSunday);
    Assertions.assertTrue(nextSunday2.equals(nextSunday));

    final LocalDate nextSunday3 = DateUtils.getNextSundayAlways(nextSunday);

    Assertions.assertTrue(nextSunday3.isAfter(nextSunday));
    Assertions.assertTrue(nextSunday3.getDayOfWeek() == DayOfWeek.SUNDAY);
    Assertions.assertTrue(2018 == nextSunday3.getYear());
    Assertions.assertTrue(5 == nextSunday3.getMonthValue());
    Assertions.assertTrue(13 == nextSunday3.getDayOfMonth());
  }

  @Test
  public void getDatesForMonthTest() {
    final List<LocalDate> localDates = DateUtils.getDatesForMonth(Month.DECEMBER, 2018);
    final Set<LocalDate> set = new HashSet<>(localDates);

    LocalDate lastDate = localDates.get(0).minusDays(1);

    Assertions.assertTrue(localDates.size() == 31);
    Assertions.assertTrue(localDates.size() == set.size());
    for (final LocalDate localDate : localDates) {
      Assertions.assertTrue(localDate.getMonth() == Month.DECEMBER);
      Assertions.assertTrue(localDate.getYear() == 2018);
      Assertions.assertTrue(lastDate.isBefore(localDate));
      lastDate = localDate;
    }
  }

  @Test
  public void getFirstDayOfMonthTest() {
    final LocalDate firstDay = DateUtils.firstDayOfMonth(Month.AUGUST, 2018);

    Assertions.assertTrue(2018 == firstDay.getYear());
    Assertions.assertTrue(8 == firstDay.getMonthValue());
    Assertions.assertTrue(1 == firstDay.getDayOfMonth());
  }

  @Test
  public void getFirstDayOfMonthLocaldateTest() {
    final LocalDate localDate = LocalDate.of(2019, 2, 5);
    final LocalDate firstDay = DateUtils.firstDayOfMonth(localDate);

    Assertions.assertTrue(2019 == firstDay.getYear());
    Assertions.assertTrue(2 == firstDay.getMonthValue());
    Assertions.assertTrue(1 == firstDay.getDayOfMonth());
  }

  @Test
  public void getLastDayOfMonthLocaldateTest() {
    LocalDate localDate = LocalDate.of(2018, Month.FEBRUARY, 5);
    final LocalDate lastDayNormal = DateUtils.lastDayOfMonth(localDate);

    Assertions.assertTrue(2018 == lastDayNormal.getYear());
    Assertions.assertTrue(2 == lastDayNormal.getMonthValue());
    Assertions.assertTrue(28 == lastDayNormal.getDayOfMonth());

    localDate = LocalDate.of(2020, Month.FEBRUARY, 5);
    final LocalDate lastDaySpecial = DateUtils.lastDayOfMonth(localDate);

    Assertions.assertTrue(2020 == lastDaySpecial.getYear());
    Assertions.assertTrue(2 == lastDaySpecial.getMonthValue());
    Assertions.assertTrue(29 == lastDaySpecial.getDayOfMonth());
  }

  @Test
  public void getLastDayOfMonthTest() {
    final LocalDate lastDayNormal = DateUtils.lastDayOfMonth(Month.FEBRUARY, 2018);

    Assertions.assertTrue(2018 == lastDayNormal.getYear());
    Assertions.assertTrue(2 == lastDayNormal.getMonthValue());
    Assertions.assertTrue(28 == lastDayNormal.getDayOfMonth());

    final LocalDate lastDaySpecial = DateUtils.lastDayOfMonth(Month.FEBRUARY, 2020);

    Assertions.assertTrue(2020 == lastDaySpecial.getYear());
    Assertions.assertTrue(2 == lastDaySpecial.getMonthValue());
    Assertions.assertTrue(29 == lastDaySpecial.getDayOfMonth());

  }

  @Test
  public void getDatesFromToTest() {
    final List<LocalDate> localDates = DateUtils.getLocalDatesFromTo(DateUtilsTest.FROM_DATE,
        DateUtilsTest.UNTIL_DATE);
    final Set<LocalDate> set = new HashSet<>(localDates);

    Assertions.assertTrue(localDates.size() == set.size());

    LocalDate lastDate = localDates.get(0).minusDays(1);

    for (final LocalDate localDate : localDates) {
      Assertions.assertTrue(lastDate.isBefore(localDate));
      lastDate = localDate;
    }

  }

  @Test
  public void getAllSundayForBalancingTestIncludes() {

    final LocalDate dateFrom = LocalDate.of(2019, 7, 7);
    final LocalDate dateUntil = LocalDate.of(2019, 7, 28);

    final List<LocalDate> sundays = DateUtils.getAllSundayForBalancing(dateFrom, dateUntil);

    Assertions.assertNotNull(sundays);
    Assertions.assertTrue(sundays.size() == 3);
  }

  @Test
  public void getAllSundayForBalancingTestExcludes() {

    final LocalDate dateFrom = LocalDate.of(2019, 7, 6);
    final LocalDate dateUntil = LocalDate.of(2019, 7, 29);

    final List<LocalDate> sundays = DateUtils.getAllSundayForBalancing(dateFrom, dateUntil);

    Assertions.assertNotNull(sundays);
    Assertions.assertTrue(sundays.size() == 4);
  }

}
