import {Category} from 'build/openapi/models/Category';
import CategoryModel from "@/model/CategoryModel";

export class CategoryTransformer {

  public static from(category?: CategoryModel): Category | undefined {
    if (category == undefined) {
      return undefined;
    }
    return new CategoryRest(category.id, category.name, category.fixCost);
  }

  public static to(category?: Category): CategoryModel | undefined {
    if (category == undefined) {
      return undefined;
    }
    return new CategoryModel(category.name!, category.fixCost!, category.id);
  }

}

class CategoryRest implements Category {

  private readonly _id?: string;
  private readonly _name?: string;
  private readonly _fixCost?: boolean;


  constructor(id?: string, name?: string, fixCost?: boolean) {
    this._id = id;
    this._name = name;
    this._fixCost = fixCost;
  }


  get id(): string | undefined {
    return this._id;
  }

  get name(): string | undefined {
    return this._name;
  }

  get fixCost(): boolean | undefined {
    return this._fixCost;
  }
}