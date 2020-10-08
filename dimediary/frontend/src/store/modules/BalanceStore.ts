import {Action, getModule, Module, Mutation, VuexModule} from 'vuex-module-decorators'
import store from '@/store'
import BalanceModel from "@/model/BalanceModel";
import BalanceRestService, {BalanceRequest} from "@/rest-services/BalanceRestService";


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
    balances.filter(
        balance => this._balances.find(
            balanceStore => balanceStore.bankAccountId === balance.bankAccountId &&
                balanceStore.date.equals(balance.date)) ===
            undefined).forEach(balance => this._balances.push(balance));
  }

  @Action
  loadBalances(balanceRequest: BalanceRequest) {
    let balanceRestService: BalanceRestService = new BalanceRestService();
    balanceRestService.getBalances(balanceRequest).then(balances => this.addBalances(balances));
  }

}

export default getModule(BalanceStore);