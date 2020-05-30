import { Transaction } from '../../../build/openapi/models/Transaction';
import TransactionModel from '../../model/TransactionModel';
import { LocalDate, DateTimeFormatter } from '@js-joda/core';


export class TransactionTransformer {

    private static dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(
        "yyyy-MM-dd"
    );



    public static from(transaction: Transaction): TransactionModel {
        let date: LocalDate | undefined = undefined;
        if (transaction.date != undefined) {
            date = LocalDate.from(TransactionTransformer.dateFormatter.parse(transaction.date));
        }
        return new TransactionModel(transaction.uuid, transaction.subject, date, transaction.amount)
    }

    public to(transactionModel: TransactionModel): Transaction {
        let dateString: string | undefined = undefined;
        if (transactionModel.date != undefined) {
            dateString = TransactionTransformer.dateFormatter.format(transactionModel.date);
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