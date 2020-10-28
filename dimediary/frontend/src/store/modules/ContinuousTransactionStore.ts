import {Action, getModule, Module, Mutation, VuexModule} from 'vuex-module-decorators'
import store from '@/store'
import ContinuousTransactionModel from "@/model/ContinuousTransactionModel";
import ContinuousRestService, {LoadContinuousTransactionRequestImpl} from "@/rest-services/ContinuousRestService";
import TransactionStore from "@/store/modules/TransactionStore";
import {LocalDate} from "@js-joda/core";


@Module({
  namespaced: true,
  name: 'ContinuousTransactionStore',
  store,
  dynamic: true
})
export class ContinuousTransactionStore extends VuexModule {

  private _continuousTransactions: ContinuousTransactionModel[] = [];
  private continuousTransactionRestService: ContinuousRestService = new ContinuousRestService();
  private _selectedContinuousTransaction: ContinuousTransactionModel | undefined = undefined;
  private _recurrenceDatesFromRecurrenceSettings: LocalDate[] = [];


  get recurrenceDatesFromRecurrenceSettings(): LocalDate[] {
    return this._recurrenceDatesFromRecurrenceSettings;
  }

  get continuousTransactions(): ContinuousTransactionModel[] {
    return this._continuousTransactions;
  }

  get selectedContinuousTransaction(): ContinuousTransactionModel | undefined {
    return this._selectedContinuousTransaction;
  }

  @Mutation
  setRecurrenceDatesFromRecurrenceSettings(value: LocalDate[]) {
    this._recurrenceDatesFromRecurrenceSettings = value;
  }

  @Mutation
  setSelectedContinuousTransaction(value: ContinuousTransactionModel | undefined) {
    this._selectedContinuousTransaction = value;
  }

  @Mutation
  addContinuousTransactions(continuousTransaction: ContinuousTransactionModel) {
    this._continuousTransactions =
        this._continuousTransactions.filter(value => value.id !== continuousTransaction.id);
    this._continuousTransactions.push(continuousTransaction);
  }

  @Mutation
  removeContinuousTransdaction(continuousTransactionId: string) {
    this._continuousTransactions =
        this._continuousTransactions.filter(value => value.id !== continuousTransactionId);
  }

  @Action
  public loadRecurrenceDates(): Promise<LocalDate[]> {
    return new Promise<LocalDate[]>((resolve, reject) => {
      if (this.selectedContinuousTransaction !== undefined) {
        this.continuousTransactionRestService.getRecurrenceDates(
            this.selectedContinuousTransaction!.recurrenceSettings,
            this.selectedContinuousTransaction!.dateBegin).
        then(value => {
          this.setRecurrenceDatesFromRecurrenceSettings(value);
          resolve(value);
        });
      }
      else {
        reject("no continuous transaction selected");
      }
    })
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
  public delete(continuousTransactionId: string): Promise<void> {
    return new Promise<void>(resolve => {
      this.continuousTransactionRestService.delete(continuousTransactionId).
      then(value => {
        this.removeContinuousTransdaction(continuousTransactionId);
        TransactionStore.removeTransactionsForContinuousTransaction(continuousTransactionId);
        resolve();
      });
    })
  }

  @Action
  public saveContinuousTransaction(continuousTransaction: ContinuousTransactionModel) {
    this.continuousTransactionRestService.saveContinuousTransaction(continuousTransaction).
    then(value => this.addContinuousTransactions(value));
  }

}

export default getModule(ContinuousTransactionStore);