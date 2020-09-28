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


  public get transactions(): TransactionModel[] {
    return this._transactions;
  }

  @Mutation
  addTransaction(transaction: TransactionModel) {
    this._transactions.push(transaction);
  }


  @Action
  loadTransactions(transactionGetRequest: TransactionGetRequestImpl) {
    let transactionsService: TransactionRestService = new TransactionRestService();
    transactionsService.getTransactions(transactionGetRequest).then((transactionModels) => {
      this.clearTransactions();
      if (transactionModels != undefined && transactionModels.length != 0) {
        transactionModels!.forEach(transaction => {
          this.addTransaction(transaction);
        })
      }
    })
  }

  @Mutation
  private clearTransactions() {
    this._transactions = [];

  }
}

export default getModule(TransactionStore);