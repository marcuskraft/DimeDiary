import {Action, getModule, Module, Mutation, VuexModule} from 'vuex-module-decorators'
import store from '@/store'
import BalanceModel from "@/model/BalanceModel";
import BalanceRestService, {BalanceRequest} from "@/rest-services/BalanceRestService";
import {LocalDate} from "@js-joda/core";
import TimeService from "@/helper/TimeService";


// @ts-ignore
@Module({
  namespaced: true,
  name: 'BalanceStore',
  store,
  dynamic: true
})
export class BalanceStore extends VuexModule {

  private _balances: BalanceModel[] = [];


  get balances(): BalanceModel[] {
    return this._balances;
  }


  @Mutation
  setBalances(value: BalanceModel[]) {
    this._balances = value;
  }

  @Mutation
  addBalances(balances: BalanceModel[]) {
    this._balances = this._balances.filter(balanceStore => balances.find(
        balance => balance.date.isEqual(balanceStore.date) && balance.bankAccountId ===
            balanceStore.bankAccountId) === undefined);
    this._balances = this._balances.concat(balances);

  }

  @Action
  loadBalances(balanceRequest: BalanceRequest) {
    let balanceRestService: BalanceRestService = new BalanceRestService();
    balanceRestService.getBalances(balanceRequest).then(balances => this.addBalances(balances));
  }

  @Action
  reloadBalances(bankAccountId: string) {
    if (this._balances.length !== 0) {
      let balanceModels = this._balances.filter(
          balance => balance.bankAccountId === bankAccountId).
      sort((a, b) => a.date.compareTo(b.date));
      if (balanceModels.length !== 0) {
        let localDateFrom: LocalDate = balanceModels[0].date;
        let localDateUntil: LocalDate = balanceModels[balanceModels.length - 1].date;
        let balanceRequest: BalanceRequest = new BalanceRequest(bankAccountId,
            TimeService.localDateToIsoString(localDateFrom),
            TimeService.localDateToIsoString(localDateUntil));
        this.loadBalances(balanceRequest);
      }


    }


  }

}

export default getModule(BalanceStore);