import {DayOfWeekAPI} from '@/../build/openapi/models/DayOfWeekAPI.ts';
import {ContinuousTransaction} from '@/../build/openapi/models/ContinuousTransaction.ts';
import {RecurrenceSettings} from '@/../build/openapi/models/RecurrenceSettings.ts';
import {BankAccount} from '@/../build/openapi/models/BankAccount.ts';
import {RecurrenceType} from '@/../build/openapi/models/RecurrenceType.ts';
import {Category} from "@/../build/openapi/models/Category.ts";
import ContinuousTransactionModel from "@/model/ContinuousTransactionModel";
import TimeService from "@/helper/TimeService";
import {BankAccountTransformer} from "@/rest-services/transformer/BankAccountTransformer";
import {CategoryTransformer} from "@/rest-services/transformer/CategoryTransformer";
import RecurrenceSettingsModel, {RecurrenceTypeModel} from "@/model/RecurrenceSettingsModel";
import {DayOfWeek} from "@js-joda/core";

export class ContinuousTransactionTransformer {

  public static from(continuousTransaction?: ContinuousTransactionModel): ContinuousTransaction | undefined {
    if (continuousTransaction == undefined) {
      return undefined;
    }
    return new ContinuousTransactionRest(continuousTransaction.id, continuousTransaction.name,
        continuousTransaction.amountEuroCent,
        TimeService.localDateToIsoString(continuousTransaction.dateBegin),
        BankAccountTransformer.from(continuousTransaction.bankAccount),
        CategoryTransformer.from(continuousTransaction.category),
        this.fromRecurrenceSettings(continuousTransaction.recurrenceSettings),
        continuousTransaction.fixCost, continuousTransaction.recurrenceExceptions !==
        undefined ? continuousTransaction.recurrenceExceptions.map(
            localDate => TimeService.localDateToIsoString(localDate)) : undefined,
        continuousTransaction.recurrenceExtraInstances !== undefined ?
            continuousTransaction.recurrenceExtraInstances.map(
                localDate => TimeService.localDateToIsoString(localDate)) : undefined);
  }

  public static to(continuousTransaction?: ContinuousTransaction): ContinuousTransactionModel | undefined {
    if (continuousTransaction == undefined) {
      return undefined;
    }
    return new ContinuousTransactionModel(continuousTransaction.name!,
        continuousTransaction.amountEuroCent!,
        TimeService.isoStringToLocalDate(continuousTransaction.dateBegin!),
        BankAccountTransformer.to(continuousTransaction.bankAccount)!,
        CategoryTransformer.to(continuousTransaction.category)!,
        this.toRecurrenceSettings(continuousTransaction.recurrenceSettings!),
        continuousTransaction.fixCost!, continuousTransaction.recurrenceExceptions !==
        undefined ? continuousTransaction.recurrenceExceptions.map(
            localDateString => TimeService.isoStringToLocalDate(localDateString)) : [],
        continuousTransaction.recurrenceExtraInstances !== undefined ?
            continuousTransaction.recurrenceExtraInstances.map(
                localDateString => TimeService.isoStringToLocalDate(localDateString)) : [],
        continuousTransaction.id);
  }

  private static toRecurrenceSettings(recurrenceSettings: RecurrenceSettings): RecurrenceSettingsModel {
    let recurrence: RecurrenceSettingsModel = new RecurrenceSettingsModel(
        this.toRecurrenceType(recurrenceSettings.recurrenceType!));
    recurrence.count = recurrenceSettings.count;
    recurrence.dayOfMonth = recurrenceSettings.dayOfMonth;
    recurrence.dayOfWeeks =
        recurrenceSettings.dayOfWeeks !== undefined ? recurrenceSettings.dayOfWeeks.map(
            dayOfWeek => this.toDayOfWeek(dayOfWeek)) : undefined;
    recurrence.interval = recurrenceSettings.interval;
    recurrence.isDayOfMonthFromBehind = recurrenceSettings.isDayOfMonthFromBehind;
    recurrence.until = recurrenceSettings.until !== undefined ? TimeService.isoStringToLocalDate(
        recurrenceSettings.until) : undefined;
    return recurrence;
  }

  private static fromRecurrenceSettings(recurrenceSettings: RecurrenceSettingsModel): RecurrenceSettings {
    let recurrence: RecurrenceSettingsRest = new RecurrenceSettingsRest();
    recurrence.recurrenceType = this.fromRecurrenceType(recurrenceSettings.recurrenceType);
    recurrence.count = recurrenceSettings.count;
    recurrence.dayOfMonth = recurrenceSettings.dayOfMonth;
    recurrence.dayOfWeeks =
        recurrenceSettings.dayOfWeeks !== undefined ? recurrenceSettings.dayOfWeeks.map(
            dayOfWeek => this.fromDayOfWeek(dayOfWeek)) : undefined;
    recurrence.interval = recurrenceSettings.interval;
    recurrence.isDayOfMonthFromBehind = recurrenceSettings.isDayOfMonthFromBehind;
    recurrence.until = recurrenceSettings.until !== undefined ? TimeService.localDateToIsoString(
        recurrenceSettings.until) : undefined;
    return recurrence;
  }

  private static toDayOfWeek(dayOfWeek: DayOfWeekAPI): DayOfWeek {
    switch (dayOfWeek) {
      case DayOfWeekAPI.MONDAY:
        return DayOfWeek.MONDAY;
      case DayOfWeekAPI.TUESDAY:
        return DayOfWeek.TUESDAY;
      case DayOfWeekAPI.WEDNESDAY:
        return DayOfWeek.WEDNESDAY;
      case DayOfWeekAPI.THURSDAY:
        return DayOfWeek.THURSDAY;
      case DayOfWeekAPI.FRIDAY:
        return DayOfWeek.FRIDAY;
      case DayOfWeekAPI.SATURDAY:
        return DayOfWeek.SATURDAY;
      case DayOfWeekAPI.SUNDAY:
      default:
        return DayOfWeek.SUNDAY;
    }
  }

  public static fromDayOfWeek(dayOfWeek: DayOfWeek): DayOfWeekAPI {
    switch (dayOfWeek) {
      case DayOfWeek.MONDAY:
        return DayOfWeekAPI.MONDAY;
      case DayOfWeek.TUESDAY:
        return DayOfWeekAPI.TUESDAY;
      case DayOfWeek.WEDNESDAY:
        return DayOfWeekAPI.WEDNESDAY;
      case DayOfWeek.THURSDAY:
        return DayOfWeekAPI.THURSDAY;
      case DayOfWeek.FRIDAY:
        return DayOfWeekAPI.FRIDAY;
      case DayOfWeek.SATURDAY:
        return DayOfWeekAPI.SATURDAY;
      case DayOfWeek.SUNDAY:
      default:
        return DayOfWeekAPI.SUNDAY;
    }
  }


  private static toRecurrenceType(recurrenceType: RecurrenceType): RecurrenceTypeModel {
    switch (recurrenceType) {
      case RecurrenceType.DAILY:
        return RecurrenceTypeModel.DAILY;
      case RecurrenceType.WEEKLY:
        return RecurrenceTypeModel.WEEKLY;
      case RecurrenceType.YEARLY:
        return RecurrenceTypeModel.YEARLY;
      case RecurrenceType.MONTHLY:
      default:
        return RecurrenceTypeModel.MONTHLY;
    }
  }

  public static fromRecurrenceType(recurrenceType: RecurrenceTypeModel): RecurrenceType {
    switch (recurrenceType) {
      case RecurrenceTypeModel.DAILY:
        return RecurrenceType.DAILY;
      case RecurrenceTypeModel.WEEKLY:
        return RecurrenceType.WEEKLY;
      case RecurrenceTypeModel.YEARLY:
        return RecurrenceType.YEARLY;
      case RecurrenceTypeModel.MONTHLY:
      default:
        return RecurrenceType.MONTHLY;
    }
  }

}

class RecurrenceSettingsRest implements RecurrenceSettings {

  recurrenceType?: RecurrenceType;
  interval?: number;
  dayOfMonth?: number;
  isDayOfMonthFromBehind?: boolean;
  dayOfWeeks?: Array<DayOfWeekAPI>;
  until?: string;
  count?: number;
  recurrenceExceptions?: Array<string>;
  recurrenceExtraInstances?: Array<string>;

}

class ContinuousTransactionRest implements ContinuousTransaction {

  id?: string;
  name?: string;
  amountEuroCent?: number;
  dateBegin?: string;
  bankAccount?: BankAccount;
  category?: Category;
  recurrenceSettings?: RecurrenceSettings;
  fixCost?: boolean;
  recurrenceExceptions?: Array<string>;
  recurrenceExtraInstances?: Array<string>;


  constructor(id?: string, name?: string, amountEuroCent?: number, dateBegin?: string,
      bankAccount?: BankAccount, category?: Category, recurrenceRule?: RecurrenceSettings,
      fixCost?: boolean, recurrenceExceptions?: Array<string>,
      recurrenceExtraInstances?: Array<string>) {
    this.id = id;
    this.name = name;
    this.amountEuroCent = amountEuroCent;
    this.dateBegin = dateBegin;
    this.bankAccount = bankAccount;
    this.category = category;
    this.recurrenceSettings = recurrenceRule;
    this.fixCost = fixCost;
    this.recurrenceExceptions = recurrenceExceptions;
    this.recurrenceExtraInstances = recurrenceExtraInstances;
  }


}