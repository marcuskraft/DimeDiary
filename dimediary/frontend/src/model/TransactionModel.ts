import {LocalDate} from '@js-joda/core';

export default class TransactionModel {

  private readonly _id?: string;
  private _subject: string;
  private _date: LocalDate;
  private _amount: number;


  constructor(subject: string, date: LocalDate, amount: number, id?: string) {
    this._id = id;
    this._subject = subject;
    this._date = date;
    this._amount = amount;
  }


  get subject(): string {
    return this._subject;
  }

  set subject(value: string) {
    this._subject = value;
  }

  get date(): LocalDate {
    return this._date;
  }

  set date(value: LocalDate) {
    this._date = value;
  }

  get amount(): number {
    return this._amount;
  }

  set amount(value: number) {
    this._amount = value;
  }
  

  get id(): string | undefined {
    return this._id;
  }
}