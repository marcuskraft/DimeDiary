import { TransactionApi } from '../../build/openapi/apis/TransactionApi';
import { Transaction } from '../../build/openapi/models/Transaction';
import { Transactions } from '../../build/openapi/models/Transactions';




export class TransactionService {
    private readonly transactionApi: TransactionApi;

    constructor() {
        this.transactionApi = new TransactionApi();
    }



    public getTransactions(): Promise<Transactions> {
        return this.transactionApi.transactionGet();
    }

}
