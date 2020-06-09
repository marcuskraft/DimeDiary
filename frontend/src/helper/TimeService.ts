import {DateTimeFormatter, LocalDate} from '@js-joda/core';

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

}