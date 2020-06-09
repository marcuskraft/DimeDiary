import {Transaction} from '../../../build/openapi/models/Transaction';
import TransactionModel from '../../model/TransactionModel';
import {LocalDate} from '@js-joda/core';
import TimeService from '@/helper/TimeService';


export class TransactionTransformer {

  public static from(transaction: Transaction): TransactionModel {
    let date: LocalDate | undefined = undefined;
    if (transaction.date != undefined) {
      date = TimeService.parseLocalDateRest(transaction.date);
    }
    return new TransactionModel(transaction.uuid, transaction.subject, date, transaction.amount)
  }

  public to(transactionModel: TransactionModel): Transaction {
    let dateString: string | undefined = undefined;
    if (transactionModel.date != undefined) {
      dateString = TimeService.formatLocalDateRest(transactionModel.date);
    }
    return new TransactionForRequest(transactionModel.uuid, transactionModel.subject, dateString, transactionModel.amount);
  }

}

export class TransactionForRequest implements Transaction {


  uuid?: string;

  subject?: string;

  amount?: number;

  date?: string;

  constructor(uuid?: string, subject?: string, date?: string, amount?: number) {
    this.uuid = uuid;
    this.subject = subject;
    this.date = date;
    this.amount = amount;
  }


}