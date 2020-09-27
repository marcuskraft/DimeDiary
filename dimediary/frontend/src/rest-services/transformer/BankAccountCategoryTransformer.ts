import BankAccountCategoryModel from "@/model/BankAccountCategoryModel";
import {BankAccountCategory} from 'build/openapi/models/BankAccountCategory';

export class BankAccountCategoryTransformer {

  public static from(bankAccountCategoryModel: BankAccountCategoryModel): BankAccountCategory {
    return new BankAccountCategoryRest(bankAccountCategoryModel.id, bankAccountCategoryModel.name,
        bankAccountCategoryModel.isRealAccount);
  }

  public static to(bankAccountCategory: BankAccountCategory): BankAccountCategoryModel {
    return new BankAccountCategoryModel(bankAccountCategory.name!,
        bankAccountCategory.isRealAccount!, bankAccountCategory.id)
  }


}

class BankAccountCategoryRest implements BankAccountCategory {

  private _id?: string;
  private _name?: string;
  private _isRealAccount?: boolean;


  constructor(id?: string, name?: string, isRealAccount?: boolean) {
    this._id = id;
    this._name = name;
    this._isRealAccount = isRealAccount;
  }


  get id(): string | undefined {
    return this._id;
  }

  get name(): string | undefined {
    return this._name;
  }

  get isRealAccount(): boolean | undefined {
    return this._isRealAccount;
  }
}
