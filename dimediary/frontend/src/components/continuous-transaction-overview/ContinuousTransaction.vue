<template>
  <v-form v-model="valid">
    <v-skeleton-loader
        class="mx-auto"
        max-width="300"
        type="card" v-if="loading"
    ></v-skeleton-loader>

    <v-card v-else max-width="50%" min-width="400px" class="mx-auto" elevation="4">
      <v-card-title>
        <span class="headline">{{ dialogTitle }}</span>
      </v-card-title>
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
                      v-model="amountEuroCent"
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
                      :local-date="dateBeginMember"
                      :rules="[ v => dateBeginMember !== undefined || 'Pflichtfeld' ]"/>
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
                                          :rules="[ v => dateBeginMember !== undefined || 'Pflichtfeld' ]"/>
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
  </v-form>
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

@Component({
  components: {DatePickerTextField}
})
export default class ContinuousTransaction extends Vue {

  @Prop({type: String}) continuousTransactionId?: string;

  private dialog: boolean = false;

  id?: string;

  nameMember: string;
  dateBeginMember: LocalDate;
  amountEuroCentMember: number;
  fixCostMember: boolean;
  bankAccountMember?: BankAccountModel;
  categoryMember?: CategoryModel;

  continuousTransaction: ContinuousTransactionModel | undefined;

  recurrenceTypeMember: RecurrenceTypeModel;
  private intervalMember: number;
  private dayOfMonthMember: number;
  private isDayOfMonthFromBehindMember: boolean;
  private selectedDayOfWeeksMember: Array<DayOfWeek>;

  private dateUntilMember?: LocalDate;
  private countMember?: number;

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
      this.loading = false;
    }
  }

  constructor() {
    super();
    this.nameMember = "";
    this.dateBeginMember = LocalDate.now();
    this.amountEuroCentMember = 0.0;
    this.fixCostMember = false;
    this.bankAccountMember = this.bankAccounts[0];
    this.categoryMember = this.categories[0];
    this.recurrenceTypeMember = RecurrenceTypeModel.MONTHLY;
    this.intervalMember = 1;
    this.dayOfMonthMember = LocalDate.now().dayOfMonth();
    this.isDayOfMonthFromBehindMember = false;
    this.selectedDayOfWeeksMember = [];
    this.dateUntilMember = LocalDate.now();
    this.countMember = 10;
    this.untilRadioMember = this.INFINITY_STRING;
  }

  private loadContinuousTransaction(continuousTransactionId: string) {
    ContinuousTransactionStore.loadContinuousTransaction(continuousTransactionId).
    then(continuousTransaction => {
      this.id = continuousTransaction.id;
      this.nameMember = continuousTransaction.name;
      this.dateBeginMember = LocalDate.from(continuousTransaction.dateBegin);
      this.amountEuroCentMember = continuousTransaction.amountEuroCent / 100;
      this.fixCostMember = continuousTransaction.fixCost;
      this.bankAccountMember = continuousTransaction.bankAccount;
      this.categoryMember = continuousTransaction.category;
      this.continuousTransaction = continuousTransaction;
      this.setDateUntil(continuousTransaction.recurrenceSettings.until);
      this.count = continuousTransaction.recurrenceSettings.count;
      this.selectedDayOfWeeksMember = continuousTransaction.recurrenceSettings.dayOfWeeks !==
      undefined ? continuousTransaction.recurrenceSettings.dayOfWeeks : [];
      this.recurrenceTypeMember = continuousTransaction.recurrenceSettings.recurrenceType;
      this.dayOfMonthMember = continuousTransaction.recurrenceSettings.dayOfMonth !==
      undefined ? continuousTransaction.recurrenceSettings.dayOfMonth : this.dateBeginMember.dayOfMonth();
      this.isDayOfMonthFromBehindMember =
          continuousTransaction.recurrenceSettings.isDayOfMonthFromBehind !==
          undefined ? continuousTransaction.recurrenceSettings.isDayOfMonthFromBehind : false;

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

  get hintIntervall(): string {
    switch (this.recurrenceTypeMember) {
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

  get dayOfWeeks(): string[] {
    return DayOfWeek.values().map(value => this.fromDayOfWeek(value));
  }

  set selectedDayOfWeeks(values: string[]) {
    this.selectedDayOfWeeksMember =
        values.map(value => this.toDayOfWeek(value)!).sort((a, b) => a.compareTo(b));
  }

  get selectedDayOfWeeks(): string[] {
    return this.selectedDayOfWeeksMember.map(value => this.fromDayOfWeek(value));
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

  get recurrenceTypes(): string[] {
    return [this.MONTHLY_STRING, this.YEARLY_STRING, this.WEEKLY_STRING, this.DAILY_STRING];
  }

  get isMonthly(): boolean {
    return this.recurrenceTypeMember === RecurrenceTypeModel.MONTHLY;
  }

  get isDaily(): boolean {
    return this.recurrenceTypeMember === RecurrenceTypeModel.DAILY;
  }

  get isWeekly(): boolean {
    return this.recurrenceTypeMember === RecurrenceTypeModel.WEEKLY;
  }

  get interval(): number {
    return this.intervalMember;
  }

  set interval(value: number) {
    this.intervalMember = value;
  }

  get dayOfMonth(): number {
    return this.dayOfMonthMember;
  }

  set dayOfMonth(value: number) {
    this.dayOfMonthMember = value;
  }

  get isDayOfMonthFromBehind(): boolean {
    return this.isDayOfMonthFromBehindMember;
  }

  set isDayOfMonthFromBehind(value: boolean) {
    this.isDayOfMonthFromBehindMember = value;
  }


  get dateUntil(): LocalDate | undefined {
    return this.dateUntilMember;
  }

  setDateUntil(value: LocalDate | undefined) {
    this.dateUntilMember = value;
  }

  get count(): number | undefined {
    return this.countMember;
  }

  set count(value: number | undefined) {
    this.countMember = value;
  }

  get recurrenceType(): string {
    switch (this.recurrenceTypeMember) {
      case RecurrenceTypeModel.DAILY:
        return this.DAILY_STRING;

      case RecurrenceTypeModel.WEEKLY:
        return this.WEEKLY_STRING;
      case RecurrenceTypeModel.YEARLY:
        return this.YEARLY_STRING;
      case RecurrenceTypeModel.MONTHLY:
      default:
        return this.MONTHLY_STRING;
    }
  }

  set recurrenceType(value: string) {
    if (value === this.MONTHLY_STRING) {
      this.recurrenceTypeMember = RecurrenceTypeModel.MONTHLY;
    }
    else if (value === this.DAILY_STRING) {
      this.recurrenceTypeMember = RecurrenceTypeModel.DAILY;
    }
    else if (value === this.WEEKLY_STRING) {
      this.recurrenceTypeMember = RecurrenceTypeModel.WEEKLY;
    }
    else if (value === this.YEARLY_STRING) {
      this.recurrenceTypeMember = RecurrenceTypeModel.YEARLY;
    }
  }

  get categories(): CategoryModel[] {
    return CategoryStore.categories;
  }

  get bankAccounts(): BankAccountModel[] {
    return BankAccountStore.bankAccounts;
  }

  get dialogTitle(): string {
    return this.continuousTransactionId !==
    undefined ? "Transaktionsreihe bearbeten" : "Transaktionsreihe anlegen";
  }

  get name(): string {
    return this.nameMember;
  }

  set name(value: string) {
    this.nameMember = value;
  }

  get dateBegin(): LocalDate {
    return this.dateBeginMember;
  }

  set dateBegin(value: LocalDate) {
    this.dateBeginMember = value;
  }

  get amountEuroCent(): number {
    return this.amountEuroCentMember;
  }

  set amountEuroCent(value: number) {
    this.amountEuroCentMember = value;
  }

  get fixCost(): boolean {
    return this.fixCostMember;
  }

  set fixCost(value: boolean) {
    this.fixCostMember = value;
  }

  get bankAccount(): BankAccountModel | undefined {
    return this.bankAccountMember;
  }

  set bankAccount(value: BankAccountModel | undefined) {
    this.bankAccountMember = value;
  }

  get category(): CategoryModel | undefined {
    return this.categoryMember;
  }

  set category(value: CategoryModel | undefined) {
    this.categoryMember = value;
  }


  requiredString(value: string | undefined): boolean {
    return !(value === undefined || value.length === 0);
  }

  setLocalDate(localDate: LocalDate) {
    this.dateBeginMember = localDate;
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
    return AmountHelper.onlyTwoPrecision(value);
  }

  close() {
    this.$router.back();
  }


  save() {
    let continuousTransaction: ContinuousTransactionModel;
    if (this.continuousTransaction !== undefined) {
      this.continuousTransaction.name = this.name;
      this.continuousTransaction.category = this.category!;
      this.continuousTransaction.bankAccount = this.bankAccount!;
      this.continuousTransaction.fixCost = this.fixCost;
      this.continuousTransaction.amountEuroCent = this.amountEuroCent * 100;
      this.continuousTransaction.dateBegin = this.dateBegin;
      this.continuousTransaction.recurrenceSettings = this.recurrenceSettingsModel;
      continuousTransaction = this.continuousTransaction;
    }
    else {
      continuousTransaction =
          new ContinuousTransactionModel(this.name, this.amountEuroCent * 100, this.dateBegin,
              this.bankAccount!, this.category!, this.recurrenceSettingsModel, this.fixCost, [],
              []);
    }
    ContinuousTransactionStore.saveContinuousTransaction(continuousTransaction);
    this.close();
  }

  get recurrenceSettingsModel() {
    let recurrenceSettingsModel = new RecurrenceSettingsModel(RecurrenceTypeModel.MONTHLY);
    recurrenceSettingsModel.isDayOfMonthFromBehind = this.isDayOfMonthFromBehind;
    recurrenceSettingsModel.dayOfMonth = this.isMonthly ? this.dayOfMonth : undefined;
    recurrenceSettingsModel.dayOfWeeks =
        (this.isWeekly || this.isDaily) ? this.selectedDayOfWeeksMember : undefined;
    if (this.isCount) {
      recurrenceSettingsModel.count = this.count;
    }
    else if (this.isUntil) {
      recurrenceSettingsModel.until = this.dateUntil;
    }
    recurrenceSettingsModel.interval = this.interval;
    return recurrenceSettingsModel;
  }

  deleteContinuousTransaction() {
    if (this.continuousTransactionId !== undefined) {
      this.deleting = true;
      ContinuousTransactionStore.delete(this.continuousTransaction!.id!).
      then(value => {
        this.deleting = false;
        this.close();
      });
    }
  }

}


</script>

<style scoped>

</style>