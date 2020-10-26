import {
  ContinuousTransactionApi,
  LoadContinuousTransactionRequest
} from "@/../build/openapi/apis/ContinuousTransactionApi"
import ContinuousTransactionModel from "@/model/ContinuousTransactionModel";
import {ContinuousTransactionTransformer} from "@/rest-services/transformer/ContinuousTransactionTransformer";

export default class ContinuousRestService {

  private continuousTransactionApi: ContinuousTransactionApi;


  constructor() {
    this.continuousTransactionApi = new ContinuousTransactionApi();
  }

  public loadContinuousTransactions(loadContinuousTransactionRequestImpl: LoadContinuousTransactionRequestImpl): Promise<ContinuousTransactionModel[]> {
    return new Promise<ContinuousTransactionModel[]>(resolve => {
      this.continuousTransactionApi.loadContinuousTransaction(loadContinuousTransactionRequestImpl).
      then(continuousTransactions => {
        let continousTransactionModels: ContinuousTransactionModel[] = [];
        continuousTransactions.forEach(continuousTransaction => continousTransactionModels.push(
            ContinuousTransactionTransformer.to(continuousTransaction)!));
        resolve(continousTransactionModels);
      });
    });
  }


  public loadContinuousTransaction(continuousTransactionId: string): Promise<ContinuousTransactionModel> {
    return new Promise<ContinuousTransactionModel>(resolve => {
      this.continuousTransactionApi.getContinuousTransaction(
          {continuousTransactionId: continuousTransactionId}).then(continuousTransaction => {
        resolve(ContinuousTransactionTransformer.to(continuousTransaction));
      });
    });
  }

  public saveContinuousTransaction(continuousTransaction: ContinuousTransactionModel): Promise<ContinuousTransactionModel> {
    return new Promise<ContinuousTransactionModel>(resolve => {
      this.continuousTransactionApi.saveContinuousTransaction(
          {continuousTransaction: ContinuousTransactionTransformer.from(continuousTransaction)}).
      then(continuousTransaction => resolve(
          ContinuousTransactionTransformer.to(continuousTransaction))
      )
    })
  }

  public delete(continuousTransactionId: string): Promise<void> {
    return this.continuousTransactionApi.deleteContinuousTransaction(
        {continuousTransactionId: continuousTransactionId});
  }


}

export class LoadContinuousTransactionRequestImpl implements LoadContinuousTransactionRequest {

  bankAccountId: string;
  dateFrom: string;
  dateUntil: string;


  constructor(bankAccountId: string, dateFrom: string, dateUntil: string) {
    this.bankAccountId = bankAccountId;
    this.dateFrom = dateFrom;
    this.dateUntil = dateUntil;
  }

}