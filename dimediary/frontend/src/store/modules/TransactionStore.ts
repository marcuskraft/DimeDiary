import {Action, getModule, Module, Mutation, VuexModule} from 'vuex-module-decorators'
import store from '@/store'
import TransactionModel from '@/model/TransactionModel';
import {TransactionGetRequestImpl, TransactionService} from '@/services/TransactionService';
import DayTransactions, {DayTransactionsArray} from "@/model/DayTransactions";


@Module({
  namespaced: true,
  name: 'TransactionStore',
  store,
  dynamic: true
})
export class TransactionStore extends VuexModule {


  private _transactions: DayTransactionsArray = new DayTransactionsArray([]);


  public get transactions(): DayTransactionsArray {
    return this._transactions;
  }

  @Mutation
  addTransaction(transaction: TransactionModel) {
    if (transaction.date == undefined) {
      console.error("date must not be null");
      throw console.error("date must not be null");
    }

    let dayTransactions = this._transactions.getDayTransaction(transaction.date);
    if (dayTransactions == undefined) {
      dayTransactions = new DayTransactions(transaction.date, []);
      this._transactions.addDayTransactions(dayTransactions);
    }
    dayTransactions.push(transaction);
  }

  @Mutation
  addDayTransaction(dayTransactions: DayTransactions) {
    this._transactions.addDayTransactions(dayTransactions);
  }


  @Action
  loadTransactions(transactionGetRequest: TransactionGetRequestImpl) {
    let transactionsService: TransactionService = new TransactionService();
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
    this._transactions.clear();

  }
}

export default getModule(TransactionStore);