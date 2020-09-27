import {LocalDate} from "@js-joda/core";
import BankAccountModel from "@/model/BankAccountModel";
import CategoryModel from "@/model/CategoryModel";

export default class ContinuousTransactionModel {

  private _name: string;
  private _amountEuroCent: number;
  private _dateBegin: LocalDate;
  private _bankAccount: BankAccountModel;
  private _category: CategoryModel;
  private _recurrenceRule: string;
  private _fixCost: boolean;

  private _id?: string


  constructor(name: string, amountEuroCent: number, dateBegin: LocalDate,
      bankAccount: BankAccountModel, category: CategoryModel,
      recurrenceRule: string, fixCost: boolean, id?: string) {
    this._name = name;
    this._amountEuroCent = amountEuroCent;
    this._dateBegin = dateBegin;
    this._bankAccount = bankAccount;
    this._category = category;
    this._recurrenceRule = recurrenceRule;
    this._fixCost = fixCost;
    this._id = id;
  }


  get name(): string {
    return this._name;
  }

  set name(value: string) {
    this._name = value;
  }

  get amountEuroCent(): number {
    return this._amountEuroCent;
  }

  set amountEuroCent(value: number) {
    this._amountEuroCent = value;
  }

  get dateBegin(): LocalDate {
    return this._dateBegin;
  }

  set dateBegin(value: LocalDate) {
    this._dateBegin = value;
  }

  get bankAccount(): BankAccountModel {
    return this._bankAccount;
  }

  set bankAccount(value: BankAccountModel) {
    this._bankAccount = value;
  }

  get category(): CategoryModel {
    return this._category;
  }

  set category(value: CategoryModel) {
    this._category = value;
  }

  get recurrenceRule(): string {
    return this._recurrenceRule;
  }

  set recurrenceRule(value: string) {
    this._recurrenceRule = value;
  }

  get fixCost(): boolean {
    return this._fixCost;
  }

  set fixCost(value: boolean) {
    this._fixCost = value;
  }

  get id(): string | undefined {
    return this._id;
  }

  set id(value: string | undefined) {
    this._id = value;
  }
}