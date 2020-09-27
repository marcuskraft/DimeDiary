export default class CategoryModel {

  private _name: string;
  private _fixCost: boolean;
  private _id?: string;


  constructor(name: string, fixCost: boolean, id?: string) {
    this._name = name;
    this._fixCost = fixCost;
    this._id = id;
  }


  get name(): string {
    return this._name;
  }

  set name(value: string) {
    this._name = value;
  }

  get fixCost(): boolean {
    return this._fixCost;
  }

  set fixCost(value: boolean) {
    this._fixCost = value;
  }

  get id(): string | undefined {
    return this._id;
  }

  set id(value: string | undefined) {
    this._id = value;
  }
}