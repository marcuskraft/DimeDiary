import {DateTimeFormatter, LocalDate, Month, ZoneId} from '@js-joda/core';

export default class TimeService {

  private static dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(
      "yyyy-MM-dd"
  );

  public static formatLocalDateRest(date: LocalDate): string {
    return TimeService.dateFormatter.format(date);
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
}