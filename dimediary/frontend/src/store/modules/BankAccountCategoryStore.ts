import {Action, getModule, Module, Mutation, VuexModule} from 'vuex-module-decorators'
import store from '@/store'
import BankAccountCategoryModel from "@/model/BankAccountCategoryModel";
import BankAccountCategoryRestService from "@/rest-services/BankAccountCategoryRestService";


// @ts-ignore
@Module({
  namespaced: true,
  name: 'BankAccountCategoryStore',
  store,
  dynamic: true
})
export class BankAccountCategoryStore extends VuexModule {

  private _bankAccountCategories: BankAccountCategoryModel[] = [];

  get bankAccountCategories(): BankAccountCategoryModel[] {
    return this._bankAccountCategories;
  }
  
  @Mutation
  private addBankAccountCategory(bankAccountCategory: BankAccountCategoryModel) {
    let index = this._bankAccountCategories.findIndex(value => value.id === bankAccountCategory.id);
    if (index !== -1) {
      this._bankAccountCategories.splice(index, 1);
    }
    this._bankAccountCategories.push(bankAccountCategory);
  }

  @Action
  public loadBankAccountCategories() {
    let bankAccountCategoryRestService: BankAccountCategoryRestService = new BankAccountCategoryRestService();
    bankAccountCategoryRestService.loadBankAccountCategories().then(
        bankAccountCategories => bankAccountCategories.forEach(
            bankAccountCategory => this.addBankAccountCategory(bankAccountCategory)));
  }

}

export default getModule(BankAccountCategoryStore);