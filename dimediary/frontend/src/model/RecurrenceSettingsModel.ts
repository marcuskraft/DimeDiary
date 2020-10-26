import {DayOfWeek, LocalDate} from "@js-joda/core";

export default class RecurrenceSettingsModel {


  private _recurrenceType: RecurrenceTypeModel;
  private _interval?: number;
  private _dayOfMonth?: number;
  private _isDayOfMonthFromBehind?: boolean;
  private _dayOfWeeks?: Array<DayOfWeek>;
  private _until?: LocalDate;
  private _count?: number;


  constructor(recurrenceType: RecurrenceTypeModel) {
    this._recurrenceType = recurrenceType;
  }


  get recurrenceType(): RecurrenceTypeModel {
    return this._recurrenceType;
  }

  set recurrenceType(value: RecurrenceTypeModel) {
    this._recurrenceType = value;
  }

  get interval(): number | undefined {
    return this._interval;
  }

  set interval(value: number | undefined) {
    this._interval = value;
  }

  get dayOfMonth(): number | undefined {
    return this._dayOfMonth;
  }

  set dayOfMonth(value: number | undefined) {
    this._dayOfMonth = value;
  }

  get isDayOfMonthFromBehind(): boolean | undefined {
    return this._isDayOfMonthFromBehind;
  }

  set isDayOfMonthFromBehind(value: boolean | undefined) {
    this._isDayOfMonthFromBehind = value;
  }

  get dayOfWeeks(): Array<DayOfWeek> | undefined {
    return this._dayOfWeeks;
  }

  set dayOfWeeks(value: Array<DayOfWeek> | undefined) {
    this._dayOfWeeks = value;
  }

  get until(): LocalDate | undefined {
    return this._until;
  }

  set until(value: LocalDate | undefined) {
    this._until = value;
  }

  get count(): number | undefined {
    return this._count;
  }

  set count(value: number | undefined) {
    this._count = value;
  }


}

export enum RecurrenceTypeModel {
  DAILY = 'DAILY',
  WEEKLY = 'WEEKLY',
  MONTHLY = 'MONTHLY',
  YEARLY = 'YEARLY'
}