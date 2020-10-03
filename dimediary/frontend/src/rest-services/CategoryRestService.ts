import {CategoryApi} from '../../build/openapi/apis/CategoryApi';
import CategoryModel from "@/model/CategoryModel";
import {CategoryTransformer} from "@/rest-services/transformer/CategoryTransformer";

export default class CategoryRestService {

  private categoryApi: CategoryApi;

  constructor() {
    this.categoryApi = new CategoryApi();
  }

  public loadCategories(): Promise<CategoryModel[]> {
    let categoriesRet: CategoryModel[] = [];
    return new Promise<CategoryModel[]>(resolve => {
      this.categoryApi.getCategories().then(categories => {
        categories.forEach(category => categoriesRet.push(CategoryTransformer.to(category)!));
        resolve(categoriesRet);
      })
    })
  }


}