import {Action, getModule, Module, Mutation, VuexModule} from 'vuex-module-decorators'
import store from '@/store'
import CategoryModel from "@/model/CategoryModel";
import CategoryRestService from "@/rest-services/CategoryRestService";


// @ts-ignore
@Module({
  namespaced: true,
  name: 'CategoryStore',
  store,
  dynamic: true
})
export class CategoryStore extends VuexModule {

  private _categories: CategoryModel[] = [];


  get categories(): CategoryModel[] {
    return this._categories;
  }

  @Mutation
  private addCategory(category: CategoryModel) {
    this._categories = this._categories.filter(value => value.id !== category.id)
    this._categories.push(category);
  }

  @Action
  public loadCategories() {
    let categoryRestService: CategoryRestService = new CategoryRestService();
    categoryRestService.loadCategories().
    then(categories => categories.forEach(category => this.addCategory(category)))
  }

  @Action
  public loadCategoriesIfNotPresent() {
    if (this._categories.length === 0) {
      this.loadCategories();
    }
  }

}

export default getModule(CategoryStore);