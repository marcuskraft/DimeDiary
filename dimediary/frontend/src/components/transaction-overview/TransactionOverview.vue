<template>
  <div :key="transactions.length">
    <v-row>
      <v-col cols="2">
        <v-select
            :items="bankAccounts"
            item-text="name"
            v-model="selectedBankAccount"
            :menu-props="{ maxHeight: '400'}"
            label="Bankkonten"
            return-object
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
                @click="$refs.menu.save(yearMonthString); yearMonthChanged();">
              OK
            </v-btn>
          </v-date-picker>
        </v-menu>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
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
    <transaction-group v-for="transaction in transactions"
                       :transaction-prop="transaction" :key="transaction.id"></transaction-group>
    <transaction-dialog v-if="isTransactionDialog"></transaction-dialog>
  </div>
</template>

<script lang="ts">
import {Component, Vue} from "vue-property-decorator";


import TransactionModel from "@/model/TransactionModel";
import TransactionGroup from "@/components/transaction-overview/TransactionGroup.vue";
import {DateTimeFormatter, YearMonth, ZonedDateTime, ZoneId} from "@js-joda/core";
import BankAccountStore from "@/store/modules/BankAccountStore";
import BankAccountModel from "@/model/BankAccountModel";
import TransactionService from "@/service/TransactionService";
import DialogStateStore from "@/store/modules/DialogStateStore";
import TransactionDialog from "@/components/transaction-overview/TransactionDialog.vue";


require('@js-joda/timezone');

const {
  Locale,
} = require("@js-joda/locale_de-de")


@Component({
  components: {
    TransactionDialog,
    TransactionGroup
  }
})
export default class TransactionOverview extends Vue {

  private transactionService: TransactionService = new TransactionService();

  yearMonthTemp: YearMonth = YearMonth.now();
  yearMonth: YearMonth = YearMonth.now();
  datePicker: boolean = false;

  mounted() {
    this.loadTransactions();
  }


  private readonly dateFormatterTechnical = DateTimeFormatter.ofPattern("yyyy-MM");
  private readonly dateFormatterUser = DateTimeFormatter.ofPattern("MMM yyyy");

  get yearMonthString(): string {
    return this.dateFormatterTechnical.format(this.yearMonthTemp);
  }

  set yearMonthString(date: string) {
    this.yearMonthTemp = YearMonth.parse(date, this.dateFormatterTechnical);
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


  get bankAccounts(): BankAccountModel[] {
    return BankAccountStore.bankAccounts;
  }

  get transactions(): TransactionModel[] {
    return this.transactionService.transactions.filter(value => this.isInDateRange(value));
  }

  private isInDateRange(value: TransactionModel): boolean {
    let transactionYearMonth: YearMonth = YearMonth.of(value.date.year(), value.date.month());
    return transactionYearMonth.equals(this.yearMonth);
  }

  set selectedBankAccount(bankAccount: BankAccountModel | undefined) {
    BankAccountStore.setBankAccountSelected(bankAccount);
  }

  get selectedBankAccount(): BankAccountModel | undefined {
    return BankAccountStore.bankAccountSelected;
  }

  private yearMonthChanged() {
    this.datePicker = false;
    this.yearMonth = this.yearMonthTemp;

    this.loadTransactions();

  }

  showDialog() {
    DialogStateStore.setIsTransactionDialog(true);
  }

  get isTransactionDialog(): boolean {
    return DialogStateStore.isTransactionDialog;
  }

  private loadTransactions() {
    if (this.selectedBankAccount !== undefined) {
      this.transactionService.loadTransactions(this.selectedBankAccount.id!,
          this.yearMonthTemp.atDay(1),
          this.yearMonthTemp.atEndOfMonth());
    }
  }
}
</script>

<style scoped>

</style>