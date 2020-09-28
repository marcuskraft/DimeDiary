import {GetTransactionsRequest, TransactionApi} from '../../build/openapi/apis/TransactionApi';
import TransactionModel from "@/model/TransactionModel";
import {TransactionTransformer} from "@/rest-services/transformer/TransactionTransformer";


export class TransactionRestService {
  private readonly transactionApi: TransactionApi;


  constructor() {
    this.transactionApi = new TransactionApi();
  }


  public getTransactions(transactionGetRequest: GetTransactionsRequest): Promise<TransactionModel[]> {
    let transactionModels: TransactionModel[] = [];
    return new Promise<TransactionModel[]>(resolve => {
      this.transactionApi.getTransactions(transactionGetRequest).then(transactions => {
        transactions.forEach(
            transaction => transactionModels.push(TransactionTransformer.to(transaction)));
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
