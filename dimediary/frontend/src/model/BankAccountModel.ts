import {LocalDate} from "@js-joda/core";
import BankAccountCategoryModel from "@/model/BankAccountCategoryModel";

export default class BankAccountModel {

  private _name: string;
  private _dateStartBalance: LocalDate;
  private _startBalanceEuroCent: number;
  private _bankAccountCategory: BankAccountCategoryModel;
  private _id?: string;
  private _bankName?: string;
  private _iban?: string;
  private _bic?: string;


  constructor(name: string, dateStartBalance: LocalDate, startBalanceEuroCent: number,
      bankAccountCategory: BankAccountCategoryModel, id?: string, bankName?: string,
      iban?: string, bic?: string) {
    this._name = name;
    this._dateStartBalance = dateStartBalance;
    this._startBalanceEuroCent = startBalanceEuroCent;
    this._bankAccountCategory = bankAccountCategory;
    this._id = id;
    this._bankName = bankName;
    this._iban = iban;
    this._bic = bic;
  }

  get name(): string {
    return this._name;
  }

  set name(value: string) {
    this._name = value;
  }

  get dateStartBalance(): LocalDate {
    return this._dateStartBalance;
  }

  set dateStartBalance(value: LocalDate) {
    this._dateStartBalance = value;
  }

  get startBalanceEuroCent(): number {
    return this._startBalanceEuroCent;
  }

  set startBalanceEuroCent(value: number) {
    this._startBalanceEuroCent = value;
  }

  get id(): string | undefined {
    return this._id;
  }

  set id(value: string | undefined) {
    this._id = value;
  }

  get bankName(): string | undefined {
    return this._bankName;
  }

  set bankName(value: string | undefined) {
    this._bankName = value;
  }

  get iban(): string | undefined {
    return this._iban;
  }

  set iban(value: string | undefined) {
    this._iban = value;
  }

  get bic(): string | undefined {
    return this._bic;
  }

  set bic(value: string | undefined) {
    this._bic = value;
  }


  get bankAccountCategory(): BankAccountCategoryModel {
    return this._bankAccountCategory;
  }

  set bankAccountCategory(value: BankAccountCategoryModel) {
    this._bankAccountCategory = value;
  }
}