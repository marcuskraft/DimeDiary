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
  private _isTransactionDialog: boolean = false;


  get isTransactionDialog(): boolean {
    return this._isTransactionDialog;
  }


  public get isBankAccountDialog(): boolean {
    return this._isBankAccountDialog;
  }

  @Mutation
  public setIsBankAccountDialog(value: boolean) {
    this._isBankAccountDialog = value;
  }

  @Mutation
  public setIsTransactionDialog(value: boolean) {
    this._isTransactionDialog = value;
  }

}

export default getModule(DialogStateStore);