import {Action, getModule, Module, Mutation, VuexModule} from 'vuex-module-decorators'
import store from '@/store'
import ContinuousTransactionModel from "@/model/ContinuousTransactionModel";
import ContinuousRestService, {LoadContinuousTransactionRequestImpl} from "@/rest-services/ContinuousRestService";


@Module({
  namespaced: true,
  name: 'ContinuousTransactionStore',
  store,
  dynamic: true
})
export class ContinuousTransactionStore extends VuexModule {

  private _continuousTransactions: ContinuousTransactionModel[] = [];
  private continuousTransactionRestService: ContinuousRestService = new ContinuousRestService();


  get continuousTransactions(): ContinuousTransactionModel[] {
    return this._continuousTransactions;
  }


  @Mutation
  addContinuousTransactions(continuousTransaction: ContinuousTransactionModel) {
    this._continuousTransactions =
        this._continuousTransactions.filter(value => value.id !== continuousTransaction.id);
    this._continuousTransactions.push(continuousTransaction);
  }

  @Action
  public loadContinuousTransactions(loadContinuousTransactionRequestImpl: LoadContinuousTransactionRequestImpl): Promise<ContinuousTransactionModel[]> {
    return new Promise<ContinuousTransactionModel[]>(resolve => {
      this.continuousTransactionRestService.loadContinuousTransactions(
          loadContinuousTransactionRequestImpl).then(
          continuousTransactions => {
            continuousTransactions.forEach(
                value => this.addContinuousTransactions(value));
            resolve(continuousTransactions);
          });
    });
  }

  @Action
  public loadContinuousTransaction(continuousTransactionId: string): Promise<ContinuousTransactionModel> {
    return new Promise<ContinuousTransactionModel>(resolve => {
      this.continuousTransactionRestService.loadContinuousTransaction(continuousTransactionId).
      then(value => {
        this.addContinuousTransactions(value);
        resolve(value);
      });
    })
  }

  @Action
  public saveContinuousTransaction(continuousTransaction: ContinuousTransactionModel) {

  }

}

export default getModule(ContinuousTransactionStore);