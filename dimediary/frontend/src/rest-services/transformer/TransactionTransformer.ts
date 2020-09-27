import {Transaction} from '../../../build/openapi/models/Transaction';
import TransactionModel from '../../model/TransactionModel';
import {LocalDate} from '@js-joda/core';
import TimeService from '@/helper/TimeService';


export class TransactionTransformer {

  public static from(transaction: Transaction): TransactionModel {
    let date: LocalDate = TimeService.dateToLocalDate(transaction.date!);
    return new TransactionModel(transaction.subject!, date, transaction.amountEuroCent!, transaction.id)
  }

  public static to(transactionModel: TransactionModel): Transaction {
    let date: Date = TimeService.localDateToDate(transactionModel.date);
    return new TransactionForRequest(transactionModel.id, transactionModel.subject, transactionModel.amount, date);
  }

}

class TransactionForRequest implements Transaction {


  id?: string;
  subject?: string;
  amount?: number;
  date?: Date;


  constructor(id: string | undefined, subject: string, amount: number, date: Date) {
    this.id = id;
    this.subject = subject;
    this.amount = amount;
    this.date = date;
  }
}