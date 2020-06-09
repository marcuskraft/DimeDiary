package com.dimediary.utils.date;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang3.Validate;
import org.dmfs.rfc5545.DateTime;

/**
 * class to handle date functionalities
 *
 * @author eyota
 *
 */
public class DateUtils {

	// TODO add the numberOfWeeksFutureBalancing in the options
	public final static int numberOfMonthFutureTransactions = 1200; // 100 Jahre

	public static LocalDate getLastSunday(final LocalDate localDate) {
		return DateUtils.getLastSunday(localDate, false);
	}

	public static LocalDate getLastSundayAlways(final LocalDate localDate) {
		return DateUtils.getLastSunday(localDate, true);
	}

	private static LocalDate getLastSunday(final LocalDate localDate, final boolean always) {
		if (localDate == null) {
			return null;
		}
		final int dayOfWeek = localDate.getDayOfWeek().getValue();
		if (always || dayOfWeek != 7) {
			return localDate.minusDays(dayOfWeek);
		}
		return localDate;
	}

	public static LocalDate getNextSunday(final LocalDate localDate) {
		return DateUtils.getNextSunday(localDate, false);
	}

	public static LocalDate getNextSundayAlways(final LocalDate localDate) {
		return DateUtils.getNextSunday(localDate, true);
	}

	private static LocalDate getNextSunday(final LocalDate localDate, final boolean always) {
		if (localDate == null) {
			return null;
		}
		final int dayOfWeek = localDate.getDayOfWeek().getValue();
		if (dayOfWeek == 7) {
			if (always) {
				return localDate.plusDays(dayOfWeek);
			} else {
				return localDate;
			}
		}
		return localDate.plusDays(7 - dayOfWeek);
	}


	public static List<LocalDate> getAllSundayForBalancing(final LocalDate dateFrom, final LocalDate dateUntil) {
		final List<LocalDate> sundays = new ArrayList<>();
		Validate.notNull(dateFrom);
		Validate.notNull(dateUntil);

		if (dateFrom.isAfter(dateUntil)) {
			throw new IllegalArgumentException("dateUntil should be after dateFrom");
		}

		LocalDate nextSunday = getNextSunday(dateFrom);
		while (nextSunday.isBefore(dateUntil)) {
			sundays.add(nextSunday);
			nextSunday = getNextSundayAlways(nextSunday);
		}

		return sundays;

	}

	/**
	 *
	 * @return the actual month
	 */
	public static Month getActualMonth() {
		return DateUtils.getMonth(LocalDate.now());
	}

	/**
	 *
	 * @return the actual year
	 */
	public static int getActualYear() {
		return DateUtils.getYear(LocalDate.now());
	}

	/**
	 *
	 * @param date
	 * @return year of the given date
	 */
	public static int getYear(final LocalDate date) {
		return date.getYear();
	}

	/**
	 *
	 * @param date
	 * @return month of the given date
	 */
	public static Month getMonth(final LocalDate date) {
		return date.getMonth();
	}

	/**
	 *
	 * @param month
	 * @param year
	 * @return list of dates for all days of the given month and year
	 */
	public static ArrayList<LocalDate> getDatesForMonth(final Month month, final int year) {
		final ArrayList<LocalDate> dates = new ArrayList<>();

		LocalDate actualDate = LocalDate.of(year, month, 1);
		final LocalDate lastDate = actualDate.plusMonths(1);

		while (actualDate.isBefore(lastDate)) {
			dates.add(actualDate);
			actualDate = actualDate.plusDays(1);
		}

		return dates;
	}

	public static LocalDate firstDayOfMonth(final Month month, final int year) {
		return LocalDate.of(year, month, 1);
	}

	public static LocalDate firstDayOfMonth(final LocalDate localDate) {
		return DateUtils.firstDayOfMonth(localDate.getMonth(), localDate.getYear());
	}

	public static LocalDate lastDayOfMonth(final Month month, final int year) {
		final LocalDate localDate = LocalDate.of(year, month, 1);
		return localDate.withDayOfMonth(localDate.lengthOfMonth());
	}

	public static LocalDate lastDayOfMonth(final LocalDate localDate) {
		return DateUtils.lastDayOfMonth(localDate.getMonth(), localDate.getYear());
	}

	public static DateTime localDateToDateTime(final LocalDate localDate) {
		if (localDate == null) {
			return null;
		}
		return new DateTime(TimeZone.getDefault(),
				localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
	}

	public static LocalDate dateTimeToLocalDate(final DateTime dateTime) {
		return Instant.ofEpochMilli(dateTime.getTimestamp()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static List<LocalDate> getLocalDatesFromTo(final LocalDate fromDate, final LocalDate untilDate) {
		if (!fromDate.isBefore(untilDate)) {
			return new ArrayList<>();
		}

		final List<LocalDate> dates = new ArrayList<>();

		LocalDate nextDate = fromDate;

		do {
			dates.add(nextDate);
			nextDate = nextDate.plusDays(1);
		} while (nextDate.isBefore(untilDate));

		return dates;
	}

	public static LocalDate getLastDateBefore(final LocalDate date, final List<LocalDate> localDates) {
		LocalDate retDate = null;
		localDates.sort(null);
		for (final LocalDate localDate : localDates) {
			if (!localDate.isBefore(date)) {
				return retDate;
			}
			retDate = localDate;
		}
		return retDate;
	}

	public static LocalDate getFirstDateAfter(final LocalDate date, final List<LocalDate> localDates) {
		localDates.sort(null);
		for (final LocalDate localDate : localDates) {
			if (localDate.isAfter(date)) {
				return localDate;
			}
		}
		return null;
	}

}
