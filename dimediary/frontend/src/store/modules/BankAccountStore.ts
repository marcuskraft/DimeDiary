import {Action, getModule, Module, Mutation, VuexModule} from 'vuex-module-decorators'
import store from '@/store'
import BankAccountModel from "@/model/BankAccountModel";
import BankAccountRestService from "@/rest-services/BankAccountRestService";


// @ts-ignore
@Module({
  namespaced: true,
  name: 'BankAccountStore',
  store,
  dynamic: true
})
export class BankAccountStore extends VuexModule {

  private _bankAccounts: BankAccountModel[] = [];
  private _bankAccountSelected: BankAccountModel | undefined = undefined;


  get bankAccounts(): BankAccountModel[] {
    return this._bankAccounts;
  }

  get bankAccountSelected(): BankAccountModel | undefined {
    return this._bankAccountSelected;
  }

  @Action
  loadBankAccounts() {
    let bankAccountService: BankAccountRestService = new BankAccountRestService();
    bankAccountService.loadBankAccounts().
    then(bankAccounts => {
      this.clearBankAccounts();
      bankAccounts.forEach(bankAccount => this.addBankAccount(bankAccount));
      if (this.bankAccounts.length > 0) {
        this.setBankAccountSelected(bankAccounts[0]);
      }
    });
  }

  @Mutation
  public setBankAccountSelected(bankAccount: BankAccountModel | undefined) {
    this._bankAccountSelected = bankAccount;
  }

  @Mutation
  private clearBankAccounts() {
    this._bankAccounts = [];
  }

  @Mutation
  private addBankAccount(bankAccount: BankAccountModel) {
    this._bankAccounts.push(bankAccount);
  }

}

export default getModule(BankAccountStore);