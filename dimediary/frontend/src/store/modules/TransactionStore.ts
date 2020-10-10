import {Action, getModule, Module, Mutation, VuexModule} from 'vuex-module-decorators'
import store from '@/store'
import TransactionModel from '@/model/TransactionModel';
import {
  TransactionGetRequestImpl,
  TransactionRestService
} from '@/rest-services/TransactionRestService';


@Module({
  namespaced: true,
  name: 'TransactionStore',
  store,
  dynamic: true
})
export class TransactionStore extends VuexModule {

  private _transactions: TransactionModel[] = [];

  private _selectedTransaction: TransactionModel | undefined = undefined;

  public get transactions(): TransactionModel[] {
    return this._transactions;
  }


  get selectedTransaction(): TransactionModel | undefined {
    return this._selectedTransaction;
  }

  @Mutation
  setSelectedTransaction(value: TransactionModel | undefined) {
    this._selectedTransaction = value;
  }

  @Mutation
  private addTransaction(transaction: TransactionModel) {
    this._transactions = this._transactions.filter(value => value.id !== transaction.id);
    this._transactions.push(transaction);
  }

  @Mutation
  private removeTransaction(transaction: TransactionModel) {
    this._transactions = this._transactions.filter(value => value.id !== transaction.id);
  }


  @Action
  public saveTransaction(transaction: TransactionModel): Promise<TransactionModel> {
    let transactionsService: TransactionRestService = new TransactionRestService();
    return new Promise<TransactionModel>(resolve => {
      transactionsService.saveTransaction(transaction).
      then(transactionReceived => {
        this.addTransaction(transactionReceived);
        resolve(transactionReceived);
      });
    })
  }


  @Action
  loadTransactions(transactionGetRequest: TransactionGetRequestImpl): Promise<void> {
    let transactionsService: TransactionRestService = new TransactionRestService();
    return new Promise<void>(resolve => {
      transactionsService.getTransactions(transactionGetRequest).then((transactionModels) => {
        if (transactionModels != undefined && transactionModels.length != 0) {
          transactionModels!.forEach(transaction => {
            this.addTransaction(transaction);
          })
        }
        resolve();
      })
    })
  }

  @Action
  deleteTransaction(transaction: TransactionModel): Promise<void> {
    let transactionsService: TransactionRestService = new TransactionRestService();
    return new Promise<void>(resolve => {
      transactionsService.deleteTransaction(transaction).
      then(value => {
        this.removeTransaction(transaction);
        resolve();
      });
    })
  }

  @Action
  loadTransaction(transactionId: string): Promise<TransactionModel> {
    let transactionsService: TransactionRestService = new TransactionRestService();
    return new Promise<TransactionModel>(resolve => {
      transactionsService.loadTransaction(transactionId).then(transaction => {
        this.addTransaction(transaction);
        resolve(transaction);
      })
    })
  }


}

export default getModule(TransactionStore);