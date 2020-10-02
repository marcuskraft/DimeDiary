import {ContinuousTransaction} from 'build/openapi/models/ContinuousTransaction';
import {BankAccount} from 'build/openapi/models/BankAccount';
import {Category} from 'build/openapi/models/Category';
import ContinuousTransactionModel from "@/model/ContinuousTransactionModel";
import TimeService from "@/helper/TimeService";
import {BankAccountTransformer} from "@/rest-services/transformer/BankAccountTransformer";
import {CategoryTransformer} from "@/rest-services/transformer/CategoryTransformer";

export class ContinuousTransactionTransformer {

  public static from(continuousTransaction?: ContinuousTransactionModel): ContinuousTransaction | undefined {
    if (continuousTransaction == undefined) {
      return undefined;
    }
    return new ContinuousTransactionRest(continuousTransaction.id, continuousTransaction.name,
        continuousTransaction.amountEuroCent,
        TimeService.localDateToIsoString(continuousTransaction.dateBegin),
        BankAccountTransformer.from(continuousTransaction.bankAccount),
        CategoryTransformer.from(continuousTransaction.category),
        continuousTransaction.recurrenceRule, continuousTransaction.fixCost);
  }

  public static to(continuousTransaction?: ContinuousTransaction): ContinuousTransactionModel | undefined {
    if (continuousTransaction == undefined) {
      return undefined;
    }
    return new ContinuousTransactionModel(continuousTransaction.name!,
        continuousTransaction.amountEuroCent!,
        TimeService.isoStringToLocalDate(continuousTransaction.dateBegin!),
        BankAccountTransformer.to(continuousTransaction.bankAccount)!,
        CategoryTransformer.to(continuousTransaction.category)!,
        continuousTransaction.recurrenceRule!, continuousTransaction.fixCost!,
        continuousTransaction.id);
  }

}

class ContinuousTransactionRest implements ContinuousTransaction {

  private _id?: string;
  private _name?: string;
  private _amountEuroCent?: number;
  private _dateBegin?: string;
  private _bankAccount?: BankAccount;
  private _category?: Category;
  private _recurrenceRule?: string;
  private _fixCost?: boolean;


  constructor(id?: string, name?: string, amountEuroCent?: number, dateBegin?: string,
      bankAccount?: BankAccount, category?: Category, recurrenceRule?: string, fixCost?: boolean) {
    this._id = id;
    this._name = name;
    this._amountEuroCent = amountEuroCent;
    this._dateBegin = dateBegin;
    this._bankAccount = bankAccount;
    this._category = category;
    this._recurrenceRule = recurrenceRule;
    this._fixCost = fixCost;
  }


  get id(): string | undefined {
    return this._id;
  }

  get name(): string | undefined {
    return this._name;
  }

  get amountEuroCent(): number | undefined {
    return this._amountEuroCent;
  }

  get dateBegin(): string | undefined {
    return this._dateBegin;
  }

  get bankAccount(): BankAccount | undefined {
    return this._bankAccount;
  }

  get category(): Category | undefined {
    return this._category;
  }

  get recurrenceRule(): string | undefined {
    return this._recurrenceRule;
  }

  get fixCost(): boolean | undefined {
    return this._fixCost;
  }
}