import {GetTransactionsRequest, TransactionApi} from '../../build/openapi/apis/TransactionApi';
import TransactionModel from "@/model/TransactionModel";
import {TransactionTransformer} from "@/services/transformer/TransactionTransformer";
import {Transaction} from "../../build/openapi/models";


export class TransactionService {
  private readonly transactionApi: TransactionApi;


  constructor() {
    this.transactionApi = new TransactionApi();
  }


  public getTransactions(transactionGetRequest: GetTransactionsRequest): Promise<TransactionModel[]> {
    let transactionsPromise: Promise<Array<Transaction>> = this.transactionApi.getTransactions(transactionGetRequest);
    let transactionModels: TransactionModel[] = [];
    return new Promise<TransactionModel[]>(resolve => {
      transactionsPromise.then(transactions => {
        transactions.forEach(transaction => transactionModels.push(TransactionTransformer.from(transaction)));
        resolve(transactionModels);
      });
    });
  }
}

export class TransactionGetRequestImpl implements GetTransactionsRequest {

  bankAccountId: string;
  dateFrom: Date;
  dateUntil: Date;


  constructor(bankAccountId: string, dateFrom: Date, dateUntil: Date) {
    this.bankAccountId = bankAccountId;
    this.dateFrom = dateFrom;
    this.dateUntil = dateUntil;
  }
}
