import {getModule, Module, Mutation, VuexModule} from 'vuex-module-decorators'
import store from '@/store'
import TimeService from "@/helper/TimeService";
import {Month} from "@js-joda/core";


@Module({
  namespaced: true,
  name: 'OverviewNavigationStore',
  store,
  dynamic: true
})
export class OverviewNavigationStore extends VuexModule {

  private _year: number = TimeService.actualYear();
  private _month: Month = TimeService.actualMonth();

  get year(): number {
    return this._year;
  }


  get month(): Month {
    return this._month;
  }

  @Mutation
  setYear(year: number) {
    this._year = year;
  }

  @Mutation
  setMonth(month: string) {
    this._month = Month.valueOf(month);
  }
}

export default getModule(OverviewNavigationStore);