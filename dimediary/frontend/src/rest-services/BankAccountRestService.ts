import BankAccountModel from "@/model/BankAccountModel";
import {BankAccountApi} from '../../build/openapi/apis/BankAccountApi';
import {BankAccountTransformer} from "@/rest-services/transformer/BankAccountTransformer";

export default class BankAccountRestService {

  private bankAccountApi: BankAccountApi;

  constructor() {
    this.bankAccountApi = new BankAccountApi();
  }

  public loadBankAccounts(): Promise<BankAccountModel[]> {
    let bankAccountModels: BankAccountModel[] = [];
    return new Promise<BankAccountModel[]>(resolve => {
      this.bankAccountApi.getBankAccounts().then(bankAccounts => {
        bankAccounts.forEach(
            bankAccount => bankAccountModels.push(BankAccountTransformer.to(bankAccount)!));
        resolve(bankAccountModels);
      });
    });
  }

  public saveBankAccount(bankAccount: BankAccountModel): Promise<BankAccountModel> {
    return new Promise<BankAccountModel>(resolve => {
      this.bankAccountApi.saveBankAccount({bankAccount: BankAccountTransformer.from(bankAccount)}).
      then(bankAccountRet => {
        resolve(BankAccountTransformer.to(bankAccountRet));
      });
    });
  }

}