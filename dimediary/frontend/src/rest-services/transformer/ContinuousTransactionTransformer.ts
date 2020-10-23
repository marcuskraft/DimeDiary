import {DayOfWeekAPI} from '@/../build/openapi/models/DayOfWeekAPI.ts';
import {ContinuousTransaction} from '@/../build/openapi/models/ContinuousTransaction.ts';
import {RecurrenceSettings} from '@/../build/openapi/models/RecurrenceSettings.ts';
import {BankAccount} from '@/../build/openapi/models/BankAccount.ts';
import {RecurrenceType} from '@/../build/openapi/models/RecurrenceType.ts';
import {Category} from '@/../build/openapi/models/Category.ts';
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
        continuousTransaction.fixCost);
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
        continuousTransaction.fixCost!,
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
    recurrence.isInfinite = recurrenceSettings.isInfinite;
    recurrence.recurrenceExceptions = recurrenceSettings.recurrenceExceptions !==
    undefined ? recurrenceSettings.recurrenceExceptions.map(
        localDateString => TimeService.isoStringToLocalDate(localDateString)) : undefined;
    recurrence.recurrenceExtraInstances =
        recurrenceSettings.recurrenceExtraInstances !== undefined ?
            recurrenceSettings.recurrenceExtraInstances.map(
                localDateString => TimeService.isoStringToLocalDate(localDateString)) : undefined;
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
    recurrence.isInfinite = recurrenceSettings.isInfinite;
    recurrence.recurrenceExceptions = recurrenceSettings.recurrenceExceptions !==
    undefined ? recurrenceSettings.recurrenceExceptions.map(
        localDate => TimeService.localDateToIsoString(localDate)) : undefined;
    recurrence.recurrenceExtraInstances =
        recurrenceSettings.recurrenceExtraInstances !== undefined ?
            recurrenceSettings.recurrenceExtraInstances.map(
                localDate => TimeService.localDateToIsoString(localDate)) : undefined;
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

  private static fromDayOfWeek(dayOfWeek: DayOfWeek): DayOfWeekAPI {
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

  private static fromRecurrenceType(recurrenceType: RecurrenceTypeModel): RecurrenceType {
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

  private _recurrenceType?: RecurrenceType;
  private _interval?: number;
  private _dayOfMonth?: number;
  private _isDayOfMonthFromBehind?: boolean;
  private _dayOfWeeks?: Array<DayOfWeekAPI>;
  private _isInfinite?: boolean;
  private _until?: string;
  private _count?: number;
  private _recurrenceExceptions?: Array<string>;
  private _recurrenceExtraInstances?: Array<string>;


  set recurrenceType(value: RecurrenceType | undefined) {
    this._recurrenceType = value;
  }

  set interval(value: number | undefined) {
    this._interval = value;
  }

  set dayOfMonth(value: number | undefined) {
    this._dayOfMonth = value;
  }

  set isDayOfMonthFromBehind(value: boolean | undefined) {
    this._isDayOfMonthFromBehind = value;
  }

  set dayOfWeeks(value: Array<DayOfWeekAPI> | undefined) {
    this._dayOfWeeks = value;
  }

  set isInfinite(value: boolean | undefined) {
    this._isInfinite = value;
  }

  set until(value: string | undefined) {
    this._until = value;
  }

  set count(value: number | undefined) {
    this._count = value;
  }

  set recurrenceExceptions(value: Array<string> | undefined) {
    this._recurrenceExceptions = value;
  }

  set recurrenceExtraInstances(value: Array<string> | undefined) {
    this._recurrenceExtraInstances = value;
  }


  get recurrenceType(): RecurrenceType | undefined {
    return this._recurrenceType;
  }

  get interval(): number | undefined {
    return this._interval;
  }

  get dayOfMonth(): number | undefined {
    return this._dayOfMonth;
  }

  get isDayOfMonthFromBehind(): boolean | undefined {
    return this._isDayOfMonthFromBehind;
  }

  get dayOfWeeks(): Array<DayOfWeekAPI> | undefined {
    return this._dayOfWeeks;
  }

  get isInfinite(): boolean | undefined {
    return this._isInfinite;
  }

  get until(): string | undefined {
    return this._until;
  }

  get count(): number | undefined {
    return this._count;
  }

  get recurrenceExceptions(): Array<string> | undefined {
    return this._recurrenceExceptions;
  }

  get recurrenceExtraInstances(): Array<string> | undefined {
    return this._recurrenceExtraInstances;
  }
}

class ContinuousTransactionRest implements ContinuousTransaction {

  private _id?: string;
  private _name?: string;
  private _amountEuroCent?: number;
  private _dateBegin?: string;
  private _bankAccount?: BankAccount;
  private _category?: Category;
  private _recurrenceRule?: RecurrenceSettings;
  private _fixCost?: boolean;


  constructor(id?: string, name?: string, amountEuroCent?: number, dateBegin?: string,
      bankAccount?: BankAccount, category?: Category, recurrenceRule?: RecurrenceSettings,
      fixCost?: boolean) {
    this._id = id;
    this._name = name;
    this._amountEuroCent = amountEuroCent;
    this._dateBegin = dateBegin;
    this._bankAccount = bankAccount;
    this._category = category;
    this._recurrenceRule = recurrenceRule;
    this._fixCost = fixCost;
  }


  get amountEuroCent(): number | undefined {
    return this._amountEuroCent;
  }

  get dateBegin(): string | undefined {
    return this._dateBegin;
  }

  get id(): string | undefined {
    return this._id;
  }

  get name(): string | undefined {
    return this._name;
  }


  get bankAccount(): BankAccount | undefined {
    return this._bankAccount;
  }

  get category(): Category | undefined {
    return this._category;
  }

  get recurrenceRule(): RecurrenceSettings | undefined {
    return this._recurrenceRule;
  }

  get fixCost(): boolean | undefined {
    return this._fixCost;
  }
}