import {LocalDate} from '@js-joda/core';
import BankAccountModel from "@/model/BankAccountModel";
import CategoryModel from "@/model/CategoryModel";
import ContinuousTransactionModel from "@/model/ContinuousTransactionModel";

export default class TransactionModel {

  private _name: string;
  private _date: LocalDate;
  private _amountEuroCent: number;
  private _fixCost: boolean;
  private _bankAccount?: BankAccountModel;
  private _category?: CategoryModel;
  private _continuousTransaction?: ContinuousTransactionModel;
  readonly _id?: string;


  constructor(name: string, date: LocalDate, amountEuroCent: number,
      fixCost: boolean, bankAccount?: BankAccountModel,
      category?: CategoryModel, continuousTransaction?: ContinuousTransactionModel,
      id?: string) {
    this._name = name;
    this._date = date;
    this._amountEuroCent = amountEuroCent;
    this._fixCost = fixCost;
    this._bankAccount = bankAccount;
    this._category = category;
    this._continuousTransaction = continuousTransaction;
    this._id = id;
  }

  get name(): string {
    return this._name;
  }

  set name(value: string) {
    this._name = value;
  }

  get date(): LocalDate {
    return this._date;
  }

  set date(value: LocalDate) {
    this._date = value;
  }

  get amountEuroCent(): number {
    return this._amountEuroCent;
  }

  set amountEuroCent(value: number) {
    this._amountEuroCent = value;
  }


  get id(): string | undefined {
    return this._id;
  }


  get bankAccount(): BankAccountModel | undefined {
    return this._bankAccount;
  }

  set bankAccount(value: BankAccountModel | undefined) {
    this._bankAccount = value;
  }

  get category(): CategoryModel | undefined {
    return this._category;
  }

  set category(value: CategoryModel | undefined) {
    this._category = value;
  }


  get fixCost(): boolean {
    return this._fixCost;
  }

  set fixCost(value: boolean) {
    this._fixCost = value;
  }

  get continuousTransaction(): ContinuousTransactionModel | undefined {
    return this._continuousTransaction;
  }

  set continuousTransaction(value: ContinuousTransactionModel | undefined) {
    this._continuousTransaction = value;
  }
}