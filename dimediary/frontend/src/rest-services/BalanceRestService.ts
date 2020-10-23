import {BalanceApi, GetBalancesRequest} from '@/../build/openapi/apis/BalanceApi';
import BalanceModel from "@/model/BalanceModel";
import BalanceTransformer from "@/rest-services/transformer/BalanceTransformer";

export default class BalanceRestService {

  private balanceApi: BalanceApi;


  constructor() {
    this.balanceApi = new BalanceApi();
  }

  public getBalances(balanceRequest: BalanceRequest): Promise<BalanceModel[]> {
    let balancesRet: BalanceModel[] = [];
    return new Promise<BalanceModel[]>(resolve => {
      this.balanceApi.getBalances(balanceRequest).then(balances => {
        balances.forEach(balance => balancesRet.push(BalanceTransformer.to(balance)));
        resolve(balancesRet);
      })
    })
  }

}

export class BalanceRequest implements GetBalancesRequest {
  bankAccountId: string;
  dateFrom: string;
  dateUntil: string;


  constructor(bankAccountId: string, dateFrom: string, dateUntil: string) {
    this.bankAccountId = bankAccountId;
    this.dateFrom = dateFrom;
    this.dateUntil = dateUntil;
  }

}