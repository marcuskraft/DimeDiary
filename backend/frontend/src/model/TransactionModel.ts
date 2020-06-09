import {LocalDate} from '@js-joda/core';

export default class TransactionModel {

  private _uuid?: string;
  private _subject?: string;
  private _date?: LocalDate;
  private _amount?: number;


  constructor(uuid?: string, subject?: string, date?: LocalDate, amount?: number) {
    this._uuid = uuid;
    this._subject = subject;
    this._date = date;
    this._amount = amount;
  }

  /**
   * Getter $subject
   * @return {string}
   */
  public get subject(): string | undefined {
    return this._subject;
  }

  /**
   * Getter $date
   * @return {LocalDate}
   */
  public get date(): LocalDate | undefined {
    return this._date;
  }

  /**
   * Getter $amount
   * @return {number}
   */
  public get amount(): number | undefined {
    return this._amount;
  }

  /**
   * Setter $subject
   * @param {string} value
   */
  public set subject(value: string | undefined) {
    this._subject = value;
  }

  /**
   * Setter $date
   * @param {LocalDate} value
   */
  public set date(value: LocalDate | undefined) {
    this._date = value;
  }

  /**
   * Setter $amount
   * @param {number} value
   */
  public set amount(value: number | undefined) {
    this._amount = value;
  }

  public get uuid(): string | undefined {
    return this._uuid;
  }


}