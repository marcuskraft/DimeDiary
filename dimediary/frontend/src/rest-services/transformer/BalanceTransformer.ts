import BalanceModel from "@/model/BalanceModel";
import {Balance} from 'build/openapi/models/Balance';
import TimeService from "@/helper/TimeService";

export default class BalanceTransformer {

  public static to(balance: Balance): BalanceModel {
    return new BalanceModel(balance.bankAccountId!,
        TimeService.isoStringToLocalDate(balance.date!),
        balance.balanceEuroCent!);
  }

}