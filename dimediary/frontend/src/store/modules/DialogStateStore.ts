import {getModule, Module, Mutation, VuexModule} from 'vuex-module-decorators'
import store from '@/store'


// @ts-ignore
@Module({
  namespaced: true,
  name: 'DialogStateStore',
  store,
  dynamic: true
})
export class DialogStateStore extends VuexModule {

  private _isBankAccountDialog: boolean = false;


  public get isBankAccountDialog(): boolean {
    return this._isBankAccountDialog;
  }


  @Mutation
  public setIsBankAccountDialog(value: boolean) {
    this._isBankAccountDialog = value;
  }
  
}

export default getModule(DialogStateStore);