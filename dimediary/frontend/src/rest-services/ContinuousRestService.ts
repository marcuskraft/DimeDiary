import {
  ContinuousTransactionApi,
  LoadContinuousTransactionRequest
} from "@/../build/openapi/apis/ContinuousTransactionApi.ts"
import ContinuousTransactionModel from "@/model/ContinuousTransactionModel";
import {ContinuousTransactionTransformer} from "@/rest-services/transformer/ContinuousTransactionTransformer";
import {LocalDate} from "@js-joda/core";
import TimeService from "@/helper/TimeService";
import RecurrenceSettingsModel from "@/model/RecurrenceSettingsModel";

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

  public getRecurrenceDates(recurrenceSettings: RecurrenceSettingsModel,
      dateBegin: LocalDate): Promise<LocalDate[]> {
    return new Promise<LocalDate[]>(resolve => {
      let until = recurrenceSettings.until;
      this.continuousTransactionApi.getRecurrenceDates({
        until: until !== undefined ? TimeService.localDateToIsoString(until) : undefined,
        interval: recurrenceSettings.interval!,
        dateBegin: TimeService.localDateToIsoString(dateBegin),
        isDayOfMonthFromBehind: recurrenceSettings.isDayOfMonthFromBehind,
        recurrenceType: ContinuousTransactionTransformer.fromRecurrenceType(
            recurrenceSettings.recurrenceType),
        dayOfWeeks: recurrenceSettings.dayOfWeeks !== undefined ? recurrenceSettings.dayOfWeeks.map(
            value => ContinuousTransactionTransformer.fromDayOfWeek(value)) : undefined,
        count: recurrenceSettings.count,
        dayOfMonth: recurrenceSettings.dayOfMonth
      }).then(dateStrings => {
        resolve(dateStrings.map(value => TimeService.isoStringToLocalDate(value)));
      })
    })
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