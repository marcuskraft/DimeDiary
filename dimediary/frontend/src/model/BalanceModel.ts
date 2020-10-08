import {LocalDate} from "@js-joda/core";

export default class BalanceModel {


  private _bankAccountId: string;
  private _date: LocalDate;
  private _balanceEuroCent: number;


  constructor(bankAccount: string, date: LocalDate, balanceEuroCent: number) {

    this._bankAccountId = bankAccount;
    this._date = date;
    this._balanceEuroCent = balanceEuroCent;
  }


  get bankAccountId(): string {
    return this._bankAccountId;
  }

  get date(): LocalDate {
    return this._date;
  }

  get balanceEuroCent(): number {
    return this._balanceEuroCent;
  }
}