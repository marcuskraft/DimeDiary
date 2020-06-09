import {Action, getModule, Module, Mutation, VuexModule} from 'vuex-module-decorators'
import store from '@/store'
import TransactionModel from '@/model/TransactionModel';
import {TransactionTransformer} from '@/services/transformer/TransactionTransformer';
import {TransactionService} from '@/services/TransactionService';
import TransactionModelArray from '@/model/TransactionModelArray';
import TimeService from '@/helper/TimeService';


@Module({
  namespaced: true,
  name: 'TransactionStore',
  store,
  dynamic: true
})
export class TransactionStore extends VuexModule {


  private _transactionMap: Map<string, TransactionModelArray> = new Map<string, TransactionModelArray>();

  public get transactionMap(): Map<string, TransactionModelArray> {
    return this._transactionMap;
  }

  @Mutation
  addTransactionToMap(transaction: TransactionModel) {
    if (transaction.date == undefined) {
      console.error("date must not be null");
      throw console.error("date must not be null");
    }

    if (!this._transactionMap.has(TimeService.formatLocalDateRest(transaction.date))) {
      this._transactionMap.set(TimeService.formatLocalDateRest(transaction.date), new TransactionModelArray([]));
    }

    this._transactionMap.get(TimeService.formatLocalDateRest(transaction.date))!.addTransaction(transaction);
  }

  @Mutation
  setTransactionMap(transactionMap: Map<string, TransactionModelArray>) {
    this._transactionMap = transactionMap;
  }

  @Mutation
  addTransactionPair(transactionPair: [string, TransactionModelArray]) {
    if (!this._transactionMap.has(transactionPair[0])) {
      this._transactionMap.set(transactionPair[0], transactionPair[1]);
    }
  }

  @Mutation
  clearTransactionMap() {
    this._transactionMap.clear();
  }

  @Action
  loadTransactions() {
    let transactionsService: TransactionService = new TransactionService();
    transactionsService.getTransactions().then((transactionArray) => {
      if (transactionArray.transactions != undefined) {
        transactionArray.transactions!.forEach(transaction => {
          this.addTransactionToMap(TransactionTransformer.from(transaction));
        })
      }
    })
  }


  @Action
  initializeTransactionsMap(transactionMap: Map<string, TransactionModelArray>) {
    this.clearTransactionMap();
    this.setTransactionMap(transactionMap);
  }

}

export default getModule(TransactionStore);