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
  private _selectedBankAccount: BankAccountModel | undefined = undefined;
  private _selectedBankAccounts: BankAccountModel[] = [];


  get selectedBankAccounts(): BankAccountModel[] {
    return this._selectedBankAccounts;
  }

  get bankAccounts(): BankAccountModel[] {
    return this._bankAccounts;
  }

  get selectedBankAccount(): BankAccountModel | undefined {
    return this._selectedBankAccount;
  }

  @Action
  loadBankAccounts(): Promise<BankAccountModel[]> {
    let bankAccountService: BankAccountRestService = new BankAccountRestService();
    return new Promise<BankAccountModel[]>(resolve => {
      bankAccountService.loadBankAccounts().
      then(bankAccounts => {
        this.clearBankAccounts();
        bankAccounts.forEach(bankAccount => this.addBankAccount(bankAccount));
        if (this.bankAccounts.length > 0) {
          this.setSelectedBankAccount(bankAccounts[0]);
        }
        resolve(bankAccounts);
      });
    })
  }

  @Action
  saveBankAccount(bankAccount: BankAccountModel) {
    let bankAccountService: BankAccountRestService = new BankAccountRestService();
    bankAccountService.saveBankAccount(bankAccount).then(value => this.addBankAccount(value));
  }

  @Mutation
  public setSelectedBankAccount(bankAccount: BankAccountModel | undefined) {
    this._selectedBankAccount = bankAccount;
  }

  @Mutation
  public setSelectedBankAccounts(value: BankAccountModel[]) {
    this._selectedBankAccounts = value;
  }


  @Mutation
  private clearBankAccounts() {
    this._bankAccounts = [];
  }

  @Mutation
  private addBankAccount(bankAccount: BankAccountModel) {
    this._bankAccounts = this._bankAccounts.filter(value => value.id !== bankAccount.id);
    this._bankAccounts.push(bankAccount);
  }

}

export default getModule(BankAccountStore);