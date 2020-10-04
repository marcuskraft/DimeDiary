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
    let index = this._categories.findIndex(value => value.id === category.id);
    if (index !== -1) {
      this._categories.splice(index, 1);
    }
    this._categories.push(category);
  }

  @Action
  public loadCategories() {
    let categoryRestService: CategoryRestService = new CategoryRestService();
    categoryRestService.loadCategories().
    then(categories => categories.forEach(category => this.addCategory(category)))
  }

}

export default getModule(CategoryStore);