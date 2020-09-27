export default class BankAccountCategoryModel {

  private _id?: string;
  private _name: string;
  private _isRealAccount: boolean;


  constructor(name: string, isRealAccount: boolean, id?: string) {
    this._id = id;
    this._name = name;
    this._isRealAccount = isRealAccount;
  }


  get id(): string | undefined {
    return this._id;
  }

  set id(value: string | undefined) {
    this._id = value;
  }

  get name(): string {
    return this._name;
  }

  set name(value: string) {
    this._name = value;
  }

  get isRealAccount(): boolean {
    return this._isRealAccount;
  }

  set isRealAccount(value: boolean) {
    this._isRealAccount = value;
  }
}