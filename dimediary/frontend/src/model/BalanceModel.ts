import {LocalDate} from "@js-joda/core";

export default class BalanceModel {

  private _bankAccountName: string;
  private _date: LocalDate;
  private _balanceEuroCent: number;


  constructor(bankAccount: string, date: LocalDate, balanceEuroCent: number) {
    this._bankAccountName = bankAccount;
    this._date = date;
    this._balanceEuroCent = balanceEuroCent;
  }


  get bankAccountName(): string {
    return this._bankAccountName;
  }

  get date(): LocalDate {
    return this._date;
  }

  get balanceEuroCent(): number {
    return this._balanceEuroCent;
  }
}