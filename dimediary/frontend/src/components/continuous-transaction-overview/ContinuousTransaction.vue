<template>
  <div>
    <v-card max-width="50%" min-width="400px" class="mx-auto" elevation="4">
      <v-card-title>
        <span class="headline">{{ dialogTitle }}</span>
      </v-card-title>

      <v-tabs
          v-model="selectedTab"
          align-with-title
      >
        <v-tab :href="`#tab-common`">
          Allgemein
        </v-tab>
        <v-tab :href="`#tab-dates`" @click="loadRecurrenceDates">
          Überblick
        </v-tab>
      </v-tabs>


      <v-skeleton-loader
          class="mx-auto"
          max-width="300"
          type="card" v-if="loading"
      ></v-skeleton-loader>

      <v-tabs-items v-model="selectedTab" v-else>
        <v-tab-item
            :value="`tab-common`"
        >
          <v-form v-model="valid">

            <v-card-text>
              <v-container>
                <v-row>
                  <v-col>
                    <v-row>
                      <v-col>
                        <v-text-field
                            v-model="name"
                            type="text"
                            label="Bezeichnung der Transaktion*"
                            :rules="[ v => requiredString(v) || 'Pflichtfeld' ]"
                        ></v-text-field>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col>
                        <v-text-field
                            v-model="amountEuro"
                            type="number"
                            label="Betrag*"
                            suffix="€"
                            :rules="[value => onlyTwoPrecision(value) || 'nur 2 Nachkommastellen möglich' ]"
                        ></v-text-field>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col>
                        <v-checkbox v-model="fixCost" label="Fixkosten"/>
                      </v-col>
                    </v-row>
                  </v-col>

                  <v-col>
                    <v-row>
                      <v-col>
                        <v-select
                            :items="bankAccounts"
                            item-text="name"
                            v-model="bankAccount"
                            label="Konto"
                            return-object
                        ></v-select>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col>
                        <v-select
                            :items="categories"
                            item-text="name"
                            v-model="category"
                            label="Kategorie"
                            return-object
                        ></v-select>
                      </v-col>
                    </v-row>

                  </v-col>

                </v-row>

                <v-row>
                  <v-col>
                    <v-row>
                      <v-col>
                        <v-select
                            :items="recurrenceTypes"
                            v-model="recurrenceType"
                            label="Wiederholungsmodus"
                        ></v-select>
                      </v-col>
                      <v-col>
                        <v-text-field
                            v-model="interval"
                            type="number"
                            label="Intervall"
                            :hint="hintIntervall"
                            :rules="[value => onlyIntegers(value) || 'nur ganze Zahlen möglich',
                               value => requiredNumber(value) || 'Pflichtfeld' ,
                               value => onlyPositiveNumbers(value) || 'Muss eine positive ganze Zahl größer 0 sein' ]"
                        ></v-text-field>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col>
                        <date-picker-text-field
                            :set-local-date="setLocalDate"
                            label="Beginn der Wiederholung*"
                            :local-date="dateBegin"
                            :rules="[ v => dateBegin !== undefined || 'Pflichtfeld' ]"/>
                      </v-col>
                    </v-row>
                    <v-row v-if="isMonthly">
                      <v-col>
                        <v-text-field
                            v-model="dayOfMonth"
                            type="number"
                            label="Tag des Monats"
                            hint="Tag des Moants, an dem sich diese Transaktion wiederholen soll!"
                            :disabled="isDayOfMonthFromBehind"
                            :rules="[value => onlyIntegers(value) || 'nur ganze Zahlen möglich',
                               value => requiredNumber(value) || 'Pflichtfeld' ,
                               value => onlyPositiveNumbers(value) || 'Muss eine positive ganze Zahl größer 0 sein' ]"
                        ></v-text-field>
                      </v-col>
                      <v-col>
                        <v-checkbox v-model="isDayOfMonthFromBehind" label="Am Ende des Monats?"/>
                      </v-col>
                    </v-row>
                    <v-row v-if="isDaily || isWeekly">
                      <v-col>
                        <v-select
                            :items="dayOfWeeks"
                            v-model="selectedDayOfWeeks"
                            label="Wochentage"
                            multiple
                        ></v-select>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col>
                        <v-radio-group v-model="untilRadio" row>
                          <v-radio
                              :key="INFINITY_STRING"
                              :label="INFINITY_STRING"
                              :value="INFINITY_STRING"
                          ></v-radio>
                          <v-radio
                              :key="COUNT_STRING"
                              :label="COUNT_STRING"
                              :value="COUNT_STRING"
                          ></v-radio>
                          <v-radio
                              :key="UNTIL_STRING"
                              :label="UNTIL_STRING"
                              :value="UNTIL_STRING"
                          ></v-radio>
                        </v-radio-group>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col>
                        <v-text-field v-if="isCount"
                                      v-model="count"
                                      type="number"
                                      label="Anzahl Wiederholungen"
                                      :rules="[value => onlyIntegers(value) || 'nur ganze Zahlen möglich',
                               value => requiredNumber(value) || 'Pflichtfeld' ,
                               value => onlyPositiveNumbers(value) || 'Muss eine positive ganze Zahl größer 0 sein' ]"
                        ></v-text-field>
                        <date-picker-text-field v-if="isUntil"
                                                :set-local-date="setDateUntil"
                                                label="Beginn der Wiederholung*"
                                                :rules="[ v => dateBegin !== undefined || 'Pflichtfeld' ]"/>
                      </v-col>
                    </v-row>
                  </v-col>
                </v-row>
              </v-container>
            </v-card-text>
            <v-card-actions>
              <v-btn icon @click="dialog = true">
                <v-icon>delete</v-icon>
              </v-btn>
              <v-spacer></v-spacer>
              <v-btn
                  color="blue darken-1"
                  text
                  @click="close"
              >
                Abbrechen
              </v-btn>
              <v-btn
                  color="blue darken-1"
                  text
                  :disabled="!valid"
                  @click="save"
              >
                OK
              </v-btn>
            </v-card-actions>
          </v-form>
        </v-tab-item>
        <v-tab-item
            :value="`tab-dates`"
        >
          <v-card-text>
            <v-container>
              <v-row>
                <recurrence-dates-overview/>
              </v-row>
            </v-container>
          </v-card-text>
        </v-tab-item>
      </v-tabs-items>


    </v-card>
    <v-dialog
        v-model="dialog"
        width="500"
    >
      <v-overlay :value="deleting">
        <v-progress-circular
            indeterminate
            color="primary"
        ></v-progress-circular>
      </v-overlay>

      <v-card>
        <v-card-title class="headline grey lighten-2">
          Transaktion löschen
        </v-card-title>


        <v-alert
            border="bottom"
            dense
            type="warning"
            elevation="2">
          Soll die Transaktionsreihe und alle zugehörigen Transaktionen wirklich gelöscht werden?
        </v-alert>


        <v-divider></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
              color="primary"
              text
              @click="dialog = false">
            Abbrechen
          </v-btn>
          <v-btn
              color="primary"
              text
              @click="deleteContinuousTransaction">
            OK
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script lang="ts">
import {Component, Prop, Vue} from "vue-property-decorator";
import {DayOfWeek, LocalDate} from "@js-joda/core";
import BankAccountModel from "@/model/BankAccountModel";
import CategoryModel from "@/model/CategoryModel";
import ContinuousTransactionModel from "@/model/ContinuousTransactionModel";
import DatePickerTextField from "@/components/common/DatePickerTextField.vue";
import AmountHelper from "@/helper/AmountHelper";
import BankAccountStore from "@/store/modules/BankAccountStore";
import CategoryStore from "@/store/modules/CategoryStore";
import ContinuousTransactionStore from "../../store/modules/ContinuousTransactionStore";
import RecurrenceSettingsModel, {RecurrenceTypeModel} from "@/model/RecurrenceSettingsModel";
import RecurrenceDatesOverview
  from "@/components/continuous-transaction-overview/RecurrenceDatesOverview.vue";
import {onlyTwoPrecision} from "@/helper/amount-utils";

@Component({
  components: {RecurrenceDatesOverview, DatePickerTextField}
})
export default class ContinuousTransaction extends Vue {

  @Prop({type: String}) continuousTransactionId?: string;

  private dialog: boolean = false;

  selectedTab: string = "common";

  id ?: string;

  private untilRadioMember: string;

  valid: boolean = true;

  loading: boolean = true;
  deleting: boolean = false;

  private readonly MONTHLY_STRING = "Monatlich";
  private readonly YEARLY_STRING = "Jährlich";
  private readonly WEEKLY_STRING = "Wöchentlich";
  private readonly DAILY_STRING = "Täglich";

  private readonly MONDAY_STRING = "Montag";
  private readonly TUESDAY_STRING = "Dienstag";
  private readonly WEDNESDAY_STRING = "Mittwoch";
  private readonly THURSDAY_STRING = "Donnerstag";
  private readonly FRIDAY_STRING = "Freitag";
  private readonly SATURDAY_STRING = "Samstag";
  private readonly SUNDAY_STRING = "Sonntag";

  private readonly INFINITY_STRING = "kein Ende";
  private readonly UNTIL_STRING = "bis";
  private readonly COUNT_STRING = "Anzahl";

  created() {
    BankAccountStore.loadBankAccountsIfNotPresent();
    CategoryStore.loadCategoriesIfNotPresent();
    if (this.continuousTransactionId !== undefined) {
      this.loadContinuousTransaction(this.continuousTransactionId);
    }
    else {
      let recurrenceSettingsModel = new RecurrenceSettingsModel(RecurrenceTypeModel.MONTHLY);
      recurrenceSettingsModel.interval = 1;
      recurrenceSettingsModel.dayOfMonth = LocalDate.now().dayOfMonth();
      recurrenceSettingsModel.isDayOfMonthFromBehind = false;
      recurrenceSettingsModel.dayOfWeeks = [];
      recurrenceSettingsModel.until = undefined;
      recurrenceSettingsModel.count = undefined;

      ContinuousTransactionStore.setSelectedContinuousTransaction(new ContinuousTransactionModel(
          "", 0.0, LocalDate.now(),
          this.bankAccounts[0], this.categories[0],
          recurrenceSettingsModel, false, [],
          []));
      this.loading = false;
    }
  }

  constructor() {
    super();
    this.untilRadioMember = this.INFINITY_STRING;
  }

  private loadContinuousTransaction(continuousTransactionId: string) {
    ContinuousTransactionStore.loadContinuousTransaction(continuousTransactionId).
    then(continuousTransaction => {
      this.id = continuousTransaction.id;
      ContinuousTransactionStore.setSelectedContinuousTransaction(continuousTransaction);

      if (this.count !== undefined) {
        this.untilRadio = this.COUNT_STRING;
      }
      else if (this.dateUntil !== undefined) {
        this.untilRadio = this.UNTIL_STRING;
      }
      else {
        this.untilRadio = this.INFINITY_STRING;
      }

      this.loading = false;
    });
  }


  get untilRadio(): string {
    return this.untilRadioMember;
  }

  set untilRadio(value: string) {
    this.untilRadioMember = value;
  }

  get isCount(): boolean {
    return this.untilRadio === this.COUNT_STRING;
  }

  get isUntil(): boolean {
    return this.untilRadio === this.UNTIL_STRING;
  }

  get dayOfWeeks(): string[] {
    return DayOfWeek.values().map(value => this.fromDayOfWeek(value));
  }

  set selectedDayOfWeeks(values: string[]) {
    this.recurrenceSettings.dayOfWeeks =
        values.map(value => this.toDayOfWeek(value)!).sort((a, b) => a.compareTo(b));
  }

  get selectedDayOfWeeks(): string[] {
    let dayOfWeeksTemp = this.recurrenceSettings.dayOfWeeks;
    return dayOfWeeksTemp !== undefined ? dayOfWeeksTemp.map(
        value => this.fromDayOfWeek(value)) : [];
  }

  get recurrenceTypes(): string[] {
    return [this.MONTHLY_STRING, this.YEARLY_STRING, this.WEEKLY_STRING, this.DAILY_STRING];
  }

  get isMonthly(): boolean {
    return this.recurrenceType === this.MONTHLY_STRING;
  }

  get isDaily(): boolean {
    return this.recurrenceType === this.DAILY_STRING
  }

  get isWeekly(): boolean {
    return this.recurrenceType === this.WEEKLY_STRING
  }

  get interval(): number | undefined {
    return this.recurrenceSettings.interval;
  }

  set interval(value: number | undefined) {
    this.recurrenceSettings.interval = value;
  }

  get dayOfMonth(): number | undefined {
    return this.recurrenceSettings.dayOfMonth;
  }

  set dayOfMonth(value: number | undefined) {
    this.recurrenceSettings.dayOfMonth = value;
  }

  get isDayOfMonthFromBehind(): boolean | undefined {
    return this.recurrenceSettings.isDayOfMonthFromBehind;
  }

  set isDayOfMonthFromBehind(value: boolean | undefined) {
    this.recurrenceSettings.isDayOfMonthFromBehind = value;
  }

  get dateUntil(): LocalDate | undefined {
    return this.recurrenceSettings.until;
  }

  setDateUntil(value: LocalDate | undefined) {
    this.recurrenceSettings.until = value;
  }

  get count(): number | undefined {
    return this.recurrenceSettings.count;
  }

  set count(value: number | undefined) {
    this.recurrenceSettings.count = value;
  }

  get recurrenceSettings() {
    return this.continuousTransactionModel.recurrenceSettings;
  }

  get recurrenceType(): string {
    let recurrenceType = this.recurrenceSettings.recurrenceType;
    return this.fromRecurrenceType(recurrenceType);
  }

  set recurrenceType(value: string) {
    this.recurrenceSettings.recurrenceType =
        this.toRecurrenceType(value);
  }

  get categories(): CategoryModel[] {
    return CategoryStore.categories;
  }

  get bankAccounts(): BankAccountModel[] {
    return BankAccountStore.bankAccounts;
  }

  get dialogTitle(): string {
    return this.continuousTransactionId !==
    undefined ? "Transaktionsreihe bearbeiten" : "Transaktionsreihe anlegen";
  }

  get name(): string {
    return this.continuousTransactionModel.name;
  }

  set name(value: string) {
    this.continuousTransactionModel.name = value;
  }

  get dateBegin(): LocalDate {
    return this.continuousTransactionModel.dateBegin;
  }

  set dateBegin(value: LocalDate) {
    this.continuousTransactionModel.dateBegin = value;
  }

  get amountEuro(): number {
    return this.continuousTransactionModel.amountEuroCent / 100;
  }

  set amountEuro(value: number) {
    this.continuousTransactionModel.amountEuroCent = value * 100;
  }

  get fixCost(): boolean {
    return this.continuousTransactionModel.fixCost;
  }

  set fixCost(value: boolean) {
    this.continuousTransactionModel.fixCost = value;
  }

  get bankAccount(): BankAccountModel {
    return this.continuousTransactionModel.bankAccount;
  }

  set bankAccount(value: BankAccountModel) {
    this.continuousTransactionModel.bankAccount = value;
  }

  get category(): CategoryModel {
    return this.continuousTransactionModel.category;
  }

  set category(value: CategoryModel) {
    this.continuousTransactionModel.category = value;
  }

  get continuousTransactionModel(): ContinuousTransactionModel {
    return ContinuousTransactionStore.selectedContinuousTransaction!;
  }

  get hintIntervall(): string {
    switch (this.toRecurrenceType(this.recurrenceType)) {
      case RecurrenceTypeModel.DAILY:
        return "Jeden wie vielten Tag?";
      case RecurrenceTypeModel.WEEKLY:
        return "Jede wie vielte Woche?";
      case RecurrenceTypeModel.YEARLY:
        return "Jedes wie vielte Jahr?";
      case RecurrenceTypeModel.MONTHLY:
      default:
        return "Jeden wie vielten Monat?";
    }
  }

  close() {
    this.$router.back();
  }

  save() {
    ContinuousTransactionStore.saveContinuousTransaction(this.continuousTransactionModel);
    this.close();
  }

  deleteContinuousTransaction() {
    if (this.continuousTransactionId !== undefined) {
      this.deleting = true;
      ContinuousTransactionStore.delete(this.continuousTransactionId).
      then(value => {
        this.deleting = false;
        this.close();
      });
    }
  }

  requiredString(value: string | undefined): boolean {
    return !(value === undefined || value.length === 0);
  }

  setLocalDate(localDate: LocalDate) {
    this.continuousTransactionModel.dateBegin = localDate;
    if (this.isMonthly) {
      this.dayOfMonth = localDate.dayOfMonth();
    }
  }


  requiredNumber(value: string | undefined) {
    return !(value === undefined || value === "");
  }

  onlyPositiveNumbers(value: number | undefined) {
    if (value !== undefined) {
      return value > 0;
    }
    return true;
  }

  onlyIntegers(value: number): boolean {
    return AmountHelper.onlyIntegers(value);
  }

  onlyTwoPrecision(value: number): boolean {
    return onlyTwoPrecision(value);
  }

  toDayOfWeek(value: string): DayOfWeek | undefined {
    if (value === this.MONDAY_STRING) {
      return DayOfWeek.MONDAY;
    }
    else if (value === this.TUESDAY_STRING) {
      return DayOfWeek.TUESDAY;
    }
    else if (value === this.WEDNESDAY_STRING) {
      return DayOfWeek.WEDNESDAY;
    }
    else if (value === this.THURSDAY_STRING) {
      return DayOfWeek.THURSDAY;
    }
    else if (value === this.FRIDAY_STRING) {
      return DayOfWeek.FRIDAY;
    }
    else if (value === this.SATURDAY_STRING) {
      return DayOfWeek.SATURDAY;
    }
    else if (value === this.SUNDAY_STRING) {
      return DayOfWeek.SUNDAY;
    }
    return undefined;
  }

  fromDayOfWeek(dayOfWeek: DayOfWeek): string {
    switch (dayOfWeek) {
      case DayOfWeek.MONDAY:
        return this.MONDAY_STRING;
      case DayOfWeek.TUESDAY:
        return this.TUESDAY_STRING;
      case DayOfWeek.WEDNESDAY:
        return this.WEDNESDAY_STRING;
      case DayOfWeek.THURSDAY:
        return this.THURSDAY_STRING;
      case DayOfWeek.FRIDAY:
        return this.FRIDAY_STRING;
      case DayOfWeek.SATURDAY:
        return this.SATURDAY_STRING;
      case DayOfWeek.SUNDAY:
      default:
        return this.SUNDAY_STRING;
    }
  }

  private fromRecurrenceType(recurrenceType: RecurrenceTypeModel): string {
    let dailyString: string = this.MONTHLY_STRING;
    switch (recurrenceType) {
      case RecurrenceTypeModel.WEEKLY:
        dailyString = this.WEEKLY_STRING;
        break;
      case RecurrenceTypeModel.YEARLY:
        dailyString = this.YEARLY_STRING;
        break;
      case RecurrenceTypeModel.DAILY:
        dailyString = this.DAILY_STRING;
        break;
      case RecurrenceTypeModel.MONTHLY:
      default:
        dailyString = this.MONTHLY_STRING;
        break;
    }
    return dailyString;
  }

  private toRecurrenceType(value: string): RecurrenceTypeModel {
    let recurrenceType: RecurrenceTypeModel = RecurrenceTypeModel.MONTHLY;
    if (value === this.MONTHLY_STRING) {
      recurrenceType = RecurrenceTypeModel.MONTHLY;
    }
    else if (value === this.DAILY_STRING) {
      recurrenceType = RecurrenceTypeModel.DAILY;
    }
    else if (value === this.WEEKLY_STRING) {
      recurrenceType = RecurrenceTypeModel.WEEKLY;
    }
    else if (value === this.YEARLY_STRING) {
      recurrenceType = RecurrenceTypeModel.YEARLY;
    }
    return recurrenceType;
  }

  loadRecurrenceDates() {
    this.loading = true;
    ContinuousTransactionStore.loadRecurrenceDates().finally(() => this.loading = false);
  }

}


</script>

<style scoped>

</style>