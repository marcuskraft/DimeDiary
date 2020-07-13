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
    return new TransactionModel(transaction.id, transaction.subject, date, transaction.amount)
  }

  public to(transactionModel: TransactionModel): Transaction {
    let dateString: string | undefined = undefined;
    if (transactionModel.date != undefined) {
      dateString = TimeService.formatLocalDateRest(transactionModel.date);
    }
    return new TransactionForRequest(transactionModel.id, transactionModel.subject, dateString, transactionModel.amount);
  }

}

class TransactionForRequest implements Transaction {


  id?: number;

  subject?: string;

  amount?: number;

  date?: string;

  constructor(id?: number, subject?: string, date?: string, amount?: number) {
    this.id = id;
    this.subject = subject;
    this.date = date;
    this.amount = amount;
  }


}