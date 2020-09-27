import {LocalDate} from "@js-joda/core";
import TransactionModel from "@/model/TransactionModel";

export default class DayTransactions {

  private _date: LocalDate;
  private _transactions: TransactionModel[];


  constructor(date: LocalDate, transactions: TransactionModel[]) {
    this._date = date;
    this._transactions = transactions;
  }


  get date(): LocalDate {
    return this._date;
  }

  get transactions(): TransactionModel[] {
    return this._transactions;
  }

  clear() {
    this._transactions = [];
  }

  push(transaction: TransactionModel) {
    this._transactions.push(transaction);
  }
}

export class DayTransactionsArray {

  private _dayTransactions: DayTransactions[];

  constructor(dayTransactions: DayTransactions[]) {
    this._dayTransactions = dayTransactions;
  }

  get dayTransactions(): DayTransactions[] {
    return this._dayTransactions;
  }

  addDayTransactions(dayTransactions: DayTransactions) {
    if (this.getDayTransaction(dayTransactions.date) == undefined) {
      this._dayTransactions.push(dayTransactions);
    }
    else {
      let message = 'there is still a dayTransactions for this date: ' +
          dayTransactions.date.toString();
      console.error(message);
      throw new Error(message);
    }

  }

  getDayTransaction(localDate: LocalDate): DayTransactions | undefined {
    return this._dayTransactions.find(value => value.date.isEqual(localDate));
  }

  clear() {
    this._dayTransactions.forEach(value => value.clear());
  }
}