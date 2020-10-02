import {DateTimeFormatter, LocalDate, Month, ZoneId} from '@js-joda/core';

export default class TimeService {

  private static dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(
      "yyyy-MM-dd"
  );

  public static formatLocalDateRest(date: LocalDate): string {
    return TimeService.dateFormatter.format(date);
  }

  public static localDateToIsoString(localDate: LocalDate): string {
    return localDate.format(this.dateFormatter);
  }

  public static isoStringToLocalDate(isoString: string): LocalDate {
    return LocalDate.parse(isoString, this.dateFormatter);
  }


  public static dateToLocalDate(date?: Date): LocalDate | undefined {
    if (date == undefined) {
      return undefined;
    }
    let month = date.getMonth() + 1;
    let dayOfMonth = date.getDate();
    let fullYear = date.getFullYear();
    return LocalDate.of(fullYear, month, dayOfMonth);
  }

  public static localDateToDate(localDate: LocalDate | undefined): Date | undefined {
    if (localDate == undefined) {
      return undefined;
    }
    return new Date(localDate.year(), localDate.month().value() - 1, localDate.dayOfMonth(), 0, 0,
        0, 0,);
  }


  public static parseLocalDateRest(dateString: string): LocalDate {
    return LocalDate.from(TimeService.dateFormatter.parse(dateString));
  }

  public static actualYear(): number {
    return this.getNowDate().year();
  }

  public static getAllMonths(): Month[] {
    return Month.values();
  }

  public static actualMonth() {
    return this.getNowDate().month();
  }

  private static getNowDate() {
    return LocalDate.now(ZoneId.systemDefault());
  }

  public static getDatesFor(year: number, month: Month): LocalDate[] {
    let localDates: LocalDate[] = [];

    let localDate: LocalDate = LocalDate.of(year, month, 1);
    while (localDate.month() == month) {
      localDates.push(localDate);
      localDate = localDate.plusDays(1);
    }

    return localDates;
  }
}