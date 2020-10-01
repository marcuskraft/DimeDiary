<template>
  <div>
    <v-row>
      <v-col cols="2">
        <v-select
            :items="bankAccounts"
            item-text="name"
            :value="selectedBankAccount"
            :menu-props="{ maxHeight: '400'}"
            label="Bankkonten"
            multiple
            hint="Wähle die Bankkonten, für die Transaktionen angezeigt werden sollen"
        ></v-select>
      </v-col>
      <v-col cols="2">
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
                @click="$refs.menu.save(yearMonthString); loadTransactions();">
              OK
            </v-btn>
          </v-date-picker>
        </v-menu>
      </v-col>
    </v-row>
    <transaction-group v-for="transaction in transactions"
                       :transaction-prop="transaction" :key="transaction.id"></transaction-group>
  </div>
</template>

<script lang="ts">
import {Component, Vue} from "vue-property-decorator";


import TransactionModel from "@/model/TransactionModel";
import TransactionStore from "@/store/modules/TransactionStore";
import {TransactionGetRequestImpl} from "@/rest-services/TransactionRestService";
import TimeService from "@/helper/TimeService";
import TransactionGroup from "@/components/transaction-overview/TransactionGroup.vue";
import {DateTimeFormatter, YearMonth, ZonedDateTime, ZoneId} from "@js-joda/core";
import BankAccountStore from "@/store/modules/BankAccountStore";
import BankAccountModel from "@/model/BankAccountModel";


require('@js-joda/timezone');

const {
  Locale,
} = require("@js-joda/locale_de-de")


@Component({
  components: {
    TransactionGroup
  }
})
export default class TransactionOverview extends Vue {

  yearMonth: YearMonth = YearMonth.now();

  datePicker: boolean = false;

  mounted() {
    BankAccountStore.loadBankAccounts();
  }

  private readonly dateFormatterTechnical = DateTimeFormatter.ofPattern("yyyy-MM");
  private readonly dateFormatterUser = DateTimeFormatter.ofPattern("MMM yyyy");

  get yearMonthString(): string {
    return this.dateFormatterTechnical.format(this.yearMonth);
  }

  set yearMonthString(date: string) {
    this.yearMonth = YearMonth.parse(date, this.dateFormatterTechnical);
  }

  get dateString(): string {
    let zonedDateTime = ZonedDateTime.of(this.yearMonth.year(), this.yearMonth.month().value(), 1,
        0, 0, 0,
        0,
        ZoneId.of('Europe/Berlin'));
    return zonedDateTime.format(
        this.dateFormatterUser.withLocale(Locale.GERMANY));
  }


  get bankAccounts(): BankAccountModel[] {
    return BankAccountStore.bankAccounts;
  }

  get transactions(): TransactionModel[] {
    return TransactionStore.transactions;
  }

  set selectedBankAccount(bankAccount: BankAccountModel | undefined) {
    BankAccountStore.setBankAccountSelected(bankAccount);
  }

  get selectedBankAccount(): BankAccountModel | undefined {
    return BankAccountStore.bankAccountSelected;
  }

  private loadTransactions() {
    this.datePicker = false;

    if (this.selectedBankAccount != undefined) {
      let request: TransactionGetRequestImpl = new TransactionGetRequestImpl(
          this.selectedBankAccount.id!,
          TimeService.localDateToDate(this.yearMonth.atDay(1))!,
          TimeService.localDateToDate(this.yearMonth.atEndOfMonth())!);
      TransactionStore.loadTransactions(request);
    }

  }

}
</script>

<style scoped>

</style>