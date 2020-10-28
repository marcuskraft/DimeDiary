import {LocalDate} from "@js-joda/core";

export default class RecurrenceDate {

  private _localDate: LocalDate;
  private _isActive: boolean;


  constructor(localDate: LocalDate, isActive?: boolean) {
    this._localDate = localDate;
    this._isActive = isActive !== undefined ? isActive : true;
  }

  get localDate(): LocalDate {
    return this._localDate;
  }

  set localDate(value: LocalDate) {
    this._localDate = value;
  }

  get isActive(): boolean {
    return this._isActive;
  }

  set isActive(value: boolean) {
    this._isActive = value;
  }
}