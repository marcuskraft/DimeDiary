import {BankAccountCategoryApi} from '../../build/openapi/apis/BankAccountCategoryApi';
import BankAccountCategoryModel from "@/model/BankAccountCategoryModel";
import {BankAccountCategoryTransformer} from "@/rest-services/transformer/BankAccountCategoryTransformer";

export default class BankAccountCategoryRestService {

  private bankAccountCategoryApi: BankAccountCategoryApi;

  constructor() {
    this.bankAccountCategoryApi = new BankAccountCategoryApi();
  }

  public loadBankAccountCategories(): Promise<BankAccountCategoryModel[]> {
    let bankAccountCategoriesRet: BankAccountCategoryModel[] = [];
    return new Promise<BankAccountCategoryModel[]>(resolve => {
      this.bankAccountCategoryApi.getBankAccountCategories().then(bankAccountCategories => {
        bankAccountCategories.forEach(bankAccountCategory => bankAccountCategoriesRet.push(
            BankAccountCategoryTransformer.to(bankAccountCategory)));
        resolve(bankAccountCategoriesRet);
      })
    })
  }


}