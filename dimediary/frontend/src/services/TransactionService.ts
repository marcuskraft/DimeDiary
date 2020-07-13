import {TransactionApi, TransactionGetRequest} from '../../build/openapi/apis/TransactionApi';
import {Transactions} from '../../build/openapi/models/Transactions';


export class TransactionService {
  private readonly transactionApi: TransactionApi;

  constructor() {
    this.transactionApi = new TransactionApi();
  }


  public getTransactions(transactionGetRequest: TransactionGetRequestImpl): Promise<Transactions> {
    return this.transactionApi.transactionGet(transactionGetRequest);
  }

}

export class TransactionGetRequestImpl implements TransactionGetRequest {

  dateFrom: string;
  dateUntil: string;


  constructor(dateFrom: string, dateUntil: string) {
    this.dateFrom = dateFrom;
    this.dateUntil = dateUntil;
  }
}
