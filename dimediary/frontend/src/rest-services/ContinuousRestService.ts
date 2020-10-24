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


}

export class LoadContinuousTransactionRequestImpl implements LoadContinuousTransactionRequest {

  private _bankAccountId: string;
  private _dateFrom: string;
  private _dateUntil: string;


  constructor(bankAccountId: string, dateFrom: string, dateUntil: string) {
    this._bankAccountId = bankAccountId;
    this._dateFrom = dateFrom;
    this._dateUntil = dateUntil;
  }

  get bankAccountId(): string {
    return this._bankAccountId;
  }

  set bankAccountId(value: string) {
    this._bankAccountId = value;
  }

  get dateFrom(): string {
    return this._dateFrom;
  }

  set dateFrom(value: string) {
    this._dateFrom = value;
  }

  get dateUntil(): string {
    return this._dateUntil;
  }

  set dateUntil(value: string) {
    this._dateUntil = value;
  }
}