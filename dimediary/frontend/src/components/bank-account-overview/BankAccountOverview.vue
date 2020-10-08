<template>
  <div>
    <v-row>
      <v-col cols="2">
        <v-row>
          <v-col cols="8">
            <v-select
                :items="bankAccounts"
                item-text="name"
                v-model="selectedBankAccounts"
                :menu-props="{ maxHeight: '400'}"
                label="Bankkonten"
                multiple
                hint="Wähle die Bankkonten, für die Transaktionen angezeigt werden sollen"
                return-object
                @change="loadBalances"
            ></v-select>
          </v-col>
          <v-col cols="2">
            <v-btn
                color="primary"
                dark
                @click="showDialog"
            >
              <v-icon dark>
                add
              </v-icon>
            </v-btn>
          </v-col>
        </v-row>
      </v-col>
      <v-col cols="1">
        <v-menu
            ref="menu"
            v-model="datePicker"
            :close-on-content-click="false"
            :return-value.sync="yearMonthString"
            transition="scale-transition"
            offset-y>
          <template v-slot:activator="{ on, attrs }">
            <v-text-field
                v-model="dateString"
                label="Monat"
                prepend-icon="mdi-calendar"
                readonly
                v-bind="attrs"
                v-on="on"
            ></v-text-field>
          </template>
          <v-date-picker
              v-model="yearMonthString"
              type="month"
              locale="GERMANY">
            <v-spacer></v-spacer>
            <v-btn
                text
                color="primary"
                @click="datePicker = false">
              Abbrechen
            </v-btn>
            <v-btn
                text
                color="primary"
                @click="$refs.menu.save(yearMonthString); yearMonthChanged();">
              OK
            </v-btn>
          </v-date-picker>
        </v-menu>
      </v-col>
    </v-row>
    <div :style="{width: width}"
         v-if="selectedBankAccounts.length !== 0">
      <v-simple-table fixed-header dense>
        <thead>
        <tr>
          <th class="text-right">
            Wochentag
          </th>
          <th class="text-left">
            Datum
          </th>
          <th class="text-left" v-for="bankAccount in selectedBankAccounts">
            {{ bankAccount.name }}
          </th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="date in dates" :key="date.toString()">
          <td>{{ getDayOfWeek(date) }}</td>
          <td>{{ getLocalDateString(date) }}</td>
          <td v-for="bankAccount in selectedBankAccounts">{{ getBalance(bankAccount, date) }}</td>
        </tr>
        </tbody>
      </v-simple-table>
    </div>
    <bank-account-dialog v-if="isBankAccountDialog"></bank-account-dialog>
  </div>
</template>

<script lang="ts">


import {Component, Vue} from "vue-property-decorator";
import BankAccountModel from "@/model/BankAccountModel";
import BankAccountStore from "@/store/modules/BankAccountStore";
import BankAccountDialog from "@/components/bank-account-overview/BankAccountDialog.vue";
import DialogStateStore from "@/store/modules/DialogStateStore";
import {DateTimeFormatter, LocalDate, YearMonth, ZonedDateTime, ZoneId} from "@js-joda/core";
import BalanceStore from "@/store/modules/BalanceStore";
import {BalanceRequest} from "@/rest-services/BalanceRestService";
import TimeService from "@/helper/TimeService";

require('@js-joda/timezone');

const {
  Locale,
} = require("@js-joda/locale_de-de")

@Component({
  components: {BankAccountDialog}
})
export default class BankAccountOverview extends Vue {

  datePicker: boolean = false;
  yearMonthTemp: YearMonth = YearMonth.now();
  yearMonth: YearMonth = YearMonth.now();

  private readonly dateFormatterTechnical = DateTimeFormatter.ofPattern("yyyy-MM");
  private readonly dateFormatterUser = DateTimeFormatter.ofPattern("MMM yyyy");
  private readonly dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
  private readonly dateFormatterDayOfWeek = DateTimeFormatter.ofPattern("EEEE");

  created() {
    this.loadBalances();
  }

  get dates(): LocalDate[] {
    let localDates: LocalDate[] = [];
    let date = this.yearMonth.atDay(1);
    while (date.isBefore(this.yearMonth.atEndOfMonth()) ||
    date.isEqual(this.yearMonth.atEndOfMonth())) {
      localDates.push(date);
      date = date.plusDays(1);
    }

    return localDates;
  }

  getLocalDateString(localDate: LocalDate): string {
    return this.dateFormatter.format(localDate);
  }

  getDayOfWeek(localDate: LocalDate): string {
    return this.dateFormatterDayOfWeek.withLocale(Locale.GERMANY).format(localDate);
  }

  get width(): string {
    return 200 + this.selectedBankAccounts.length * 100 + "px";
  }

  getBalance(bankAccount: BankAccountModel, localDate: LocalDate): string {
    let balanceFind = BalanceStore.balances.find(
        balance => balance.bankAccountId === bankAccount.id && balance.date.equals(localDate));
    if (balanceFind !== undefined) {
      return (balanceFind.balanceEuroCent / 100).toLocaleString(Locale.GERMANY);
    }
    return "";
  }

  get bankAccounts(): BankAccountModel[] {
    return BankAccountStore.bankAccounts;
  }

  get yearMonthString(): string {
    return this.dateFormatterTechnical.format(this.yearMonthTemp);
  }

  set yearMonthString(date: string) {
    this.yearMonthTemp = YearMonth.parse(date, this.dateFormatterTechnical);
  }

  set selectedBankAccounts(bankAccount: BankAccountModel[]) {
    BankAccountStore.setSelectedBankAccounts(bankAccount);
  }

  get selectedBankAccounts(): BankAccountModel[] {
    return BankAccountStore.selectedBankAccounts;
  }

  get isBankAccountDialog(): boolean {
    return DialogStateStore.isBankAccountDialog;
  }

  get dateFrom(): LocalDate {
    return this.yearMonth.atDay(1);
  }

  get dateUntil(): LocalDate {
    return this.yearMonth.atEndOfMonth();
  }

  showDialog() {
    DialogStateStore.setIsBankAccountDialog(true);
  }

  loadBalances() {
    this.selectedBankAccounts.forEach(
        bankAccount => BalanceStore.loadBalances(
            new BalanceRequest(bankAccount.id!, TimeService.localDateToIsoString(this.dateFrom),
                TimeService.localDateToIsoString(this.dateUntil))));
  }

  get dateString(): string {
    let zonedDateTime = ZonedDateTime.of(this.yearMonthTemp.year(),
        this.yearMonthTemp.month().value(), 1,
        0, 0, 0,
        0,
        ZoneId.of('Europe/Berlin'));
    return zonedDateTime.format(
        this.dateFormatterUser.withLocale(Locale.GERMANY));
  }

  private yearMonthChanged() {
    this.datePicker = false;
    this.yearMonth = this.yearMonthTemp;
    this.loadBalances();
  }

}


</script>

<style scoped>

</style>