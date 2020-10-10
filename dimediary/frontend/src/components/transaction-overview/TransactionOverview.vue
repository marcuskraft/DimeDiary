<template>
  <div :key="transactions.length">
    <v-container>
      <v-card class="transaction-overview" max-width="80%" min-width="400px" outlined
              elevation="2" rounded>
        <v-row>
          <v-col v-if="actualBalance !== ''" cols="12" style="margin-left: 30px">
            <v-sheet max-width="100px" align="center" rounded color="success" m
                     elevation="11">
              <b>{{ actualBalance }}</b>
            </v-sheet>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="2">
            <v-text-field outlined label="Transaktion suchen"></v-text-field>
          </v-col>
          <v-col cols="2">
            <v-select
                :items="bankAccounts"
                item-text="name"
                v-model="selectedBankAccount"
                :menu-props="{ maxHeight: '400'}"
                label="Bankkonten"
                return-object
                hint="Wähle die Bankkonten, für die Transaktionen angezeigt werden sollen"
                @change="reload"
            ></v-select>
          </v-col>
          <v-col cols="2">
            <v-select
                :items="categories"
                item-text="name"
                v-model="selectedCategories"
                label="Kategorien"
                return-object
                multiple
                clearable>
              <template v-slot:selection="{ item, index }">
                <span v-if="index === 0">{{ item.name }}</span>
                <span
                    v-if="index === 1"
                    class="grey--text caption">(+{{ selectedCategories.length - 1 }} andere)</span>
              </template>
            </v-select>
          </v-col>
          <v-col cols="2">
            <date-picker-text-field-range label="Datumsbereich"
                                          :local-dates="[dateFrom, dateUntil]"
                                          :set-local-dates="setLocalDates"></date-picker-text-field-range>
          </v-col>
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
      </v-card>

      <transaction-group v-for="transaction in transactions"
                         :id="ref(transaction)"
                         :transaction-prop="transaction"
                         :key="transaction.id"></transaction-group>


    </v-container>

    <transaction-dialog v-if="isTransactionDialog"></transaction-dialog>
  </div>
</template>

<script lang="ts">
import {Component, Vue} from "vue-property-decorator";


import TransactionModel from "@/model/TransactionModel";
import TransactionGroup from "@/components/transaction-overview/TransactionGroup.vue";
import {LocalDate, ZoneId} from "@js-joda/core";
import BankAccountStore from "@/store/modules/BankAccountStore";
import BankAccountModel from "@/model/BankAccountModel";
import TransactionService from "@/service/TransactionService";
import DialogStateStore from "@/store/modules/DialogStateStore";
import TransactionDialog from "@/components/transaction-overview/TransactionDialog.vue";
import BalanceStore from "@/store/modules/BalanceStore";
import AmountHelper from "@/helper/AmountHelper";
import CategoryModel from "@/model/CategoryModel";
import CategoryStore from "@/store/modules/CategoryStore";
import DatePickerTextFieldRange from "@/components/common/DatePickerTextFieldRange.vue";


require('@js-joda/timezone');

const {
  Locale,
} = require("@js-joda/locale_de-de")


@Component({
  components: {
    DatePickerTextFieldRange,
    TransactionDialog,
    TransactionGroup
  }
})
export default class TransactionOverview extends Vue {

  private transactionService: TransactionService = new TransactionService();

  private actualLocalDate: LocalDate = LocalDate.now(ZoneId.of("Europe/Berlin"));

  private selectedCategoriesMember: CategoryModel[] = [];

  private datesMember: LocalDate[];

  created() {
    BankAccountStore.loadBankAccounts().then(value => this.reload());
  }

  constructor() {
    super();
    let dates: LocalDate[] = [];
    for (let i = 0; i < 31; i++) {
      dates.push(this.actualLocalDate.plusDays(i));
    }
    for (let i = 1; i < 30; i++) {
      dates.push(this.actualLocalDate.minusDays(i));
    }
    this.datesMember = dates.sort((a, b) => a.compareTo(b));
  }

  get bankAccounts(): BankAccountModel[] {
    return BankAccountStore.bankAccounts;
  }

  get transactionToScrollTo(): string {
    let transactionModels = this.transactions.filter(
        value => value.date.isBefore(this.actualLocalDate) ||
            value.date.isEqual(this.actualLocalDate)).sort((a, b) => b.date.compareTo(a.date));
    if (transactionModels.length === 0) {
      return "";
    }
    return this.ref(transactionModels[0])
  }

  get transactions(): TransactionModel[] {
    return this.transactionService.transactions.filter(value => this.isInDateRange(value)).
    filter(value => value.bankAccount!.id === this.selectedBankAccount!.id).
    sort((a, b) => b.date.compareTo(a.date));
  }

  get categories(): CategoryModel[] {
    return CategoryStore.categories;
  }


  get selectedCategories(): CategoryModel[] {
    return this.selectedCategoriesMember;
  }

  set selectedCategories(value: CategoryModel[]) {
    this.selectedCategoriesMember = value;
  }

  get localDates(): LocalDate[] {
    return this.datesMember;
  }

  setLocalDates(dates: LocalDate[]) {
    this.datesMember = dates;
  }

  get actualBalance(): string {
    if (this.selectedBankAccount !== undefined) {
      let balance = BalanceStore.balances.find(
          value => value.bankAccountId === this.selectedBankAccount!.id &&
              value.date.isEqual(LocalDate.now()));
      if (balance !== undefined) {
        return AmountHelper.euroCentToStringWithEuroSign(balance.balanceEuroCent);
      }
    }
    return "";
  }

  ref(transaction: TransactionModel): string {
    return "ref" + transaction.id!;
  }

  private isInDateRange(value: TransactionModel): boolean {
    return this.localDates.find(localDate => localDate.isEqual(value.date)) !== undefined;
  }

  set selectedBankAccount(bankAccount: BankAccountModel | undefined) {
    BankAccountStore.setSelectedBankAccount(bankAccount);
  }

  get selectedBankAccount(): BankAccountModel | undefined {
    return BankAccountStore.selectedBankAccount;
  }


  showDialog() {
    DialogStateStore.setIsTransactionDialog(true);
  }

  get isTransactionDialog(): boolean {
    return DialogStateStore.isTransactionDialog;
  }

  get dateUntil() {
    return this.localDates[this.localDates.length - 1];
  }

  get dateFrom() {
    return this.localDates[0];
  }

  private reload() {
    this.loadTransactions();
  }

  private loadTransactions() {
    if (this.selectedBankAccount !== undefined) {
      this.transactionService.loadTransactions(this.selectedBankAccount.id!,
          this.dateFrom,
          this.dateUntil).
      then(value => {
        if (this.transactionToScrollTo !== "") {
          this.$vuetify.goTo('#' + this.transactionToScrollTo, {offset: 100})
        }
      });
    }
  }

}
</script>

<style scoped>


.transaction-overview {
  align-items: center;
  margin: auto;
}

</style>