import {Transaction} from '../../../build/openapi/models/Transaction';
import TransactionModel from '../../model/TransactionModel';
import TimeService from '@/helper/TimeService';
import {BankAccountTransformer} from "@/rest-services/transformer/BankAccountTransformer";
import {CategoryTransformer} from "@/rest-services/transformer/CategoryTransformer";
import {ContinuousTransactionTransformer} from "@/rest-services/transformer/ContinuousTransactionTransformer";
import {BankAccount} from 'build/openapi/models/BankAccount';
import {Category} from 'build/openapi/models/Category';
import {ContinuousTransaction} from 'build/openapi/models/ContinuousTransaction';


export class TransactionTransformer {

  public static to(transaction: Transaction): TransactionModel {
    return new TransactionModel(transaction.subject!,
        TimeService.isoStringToLocalDate(transaction.date!),
        transaction.amountEuroCent!, transaction.fixCost!,
        BankAccountTransformer.to(transaction.bankAccount),
        CategoryTransformer.to(transaction.category),
        ContinuousTransactionTransformer.to(transaction.continuousTransaction), transaction.id);
  }

  public static from(transactionModel: TransactionModel): Transaction {
    return new TransactionForRequest(transactionModel.id!, transactionModel.name,
        transactionModel.amountEuroCent, TimeService.localDateToIsoString(transactionModel.date!),
        BankAccountTransformer.from(transactionModel.bankAccount),
        CategoryTransformer.from(transactionModel.category),
        ContinuousTransactionTransformer.from(transactionModel.continuousTransaction),
        transactionModel.fixCost);
  }

}

class TransactionForRequest implements Transaction {


  private _id?: string;
  private _subject?: string;
  private _amountEuroCent?: number;
  private _date?: string;
  private _bankAccount?: BankAccount;
  private _category?: Category;
  private _continuousTransaction?: ContinuousTransaction;
  private _fixCost?: boolean;


  constructor(id?: string, subject?: string, amountEuroCent?: number, date?: string,
      bankAccount?: BankAccount, category?: Category, continuousTransaction?: ContinuousTransaction,
      fixCost?: boolean) {
    this._id = id;
    this._subject = subject;
    this._amountEuroCent = amountEuroCent;
    this._date = date;
    this._bankAccount = bankAccount;
    this._category = category;
    this._continuousTransaction = continuousTransaction;
    this._fixCost = fixCost;
  }


  get id(): string | undefined {
    return this._id;
  }

  get subject(): string | undefined {
    return this._subject;
  }

  get amountEuroCent(): number | undefined {
    return this._amountEuroCent;
  }

  get date(): string | undefined {
    return this._date;
  }

  get bankAccount(): BankAccount | undefined {
    return this._bankAccount;
  }

  get category(): Category | undefined {
    return this._category;
  }

  get continuousTransaction(): ContinuousTransaction | undefined {
    return this._continuousTransaction;
  }

  get fixCost(): boolean | undefined {
    return this._fixCost;
  }
}