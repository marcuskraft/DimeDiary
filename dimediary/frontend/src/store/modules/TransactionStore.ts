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
    let index = this._transactions.findIndex(value => value.id === transaction.id);
    if (index !== -1) {
      this._transactions.splice(index, 1);
    }
    this._transactions.push(transaction);
  }


  @Action
  public saveTransaction(transaction: TransactionModel) {
    let transactionsService: TransactionRestService = new TransactionRestService();
    transactionsService.saveTransaction(transaction).
    then(transactionReceived => this.addTransaction(transactionReceived));
  }


  @Action
  loadTransactions(transactionGetRequest: TransactionGetRequestImpl) {
    let transactionsService: TransactionRestService = new TransactionRestService();
    transactionsService.getTransactions(transactionGetRequest).then((transactionModels) => {
      if (transactionModels != undefined && transactionModels.length != 0) {
        transactionModels!.forEach(transaction => {
          this.addTransaction(transaction);
        })
      }
    })
  }


}

export default getModule(TransactionStore);