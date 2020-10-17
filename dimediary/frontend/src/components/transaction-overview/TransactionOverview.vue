<template>
  <div :key="transactions.length">
    <v-skeleton-loader
        class="mx-auto"
        max-width="80%"
        type="card" v-if="isLoading"
    ></v-skeleton-loader>

    <v-card v-else class="transaction-overview" max-width="60%" min-width="400px" outlined
            elevation="2" rounded>
      <v-container>
        <v-row justify="center">
          <v-col v-if="actualBalanceString !== ''">
            <v-chip max-width="300px" rounded
                    :color="actualBalanceEuroCent >= 0 ? 'success' : 'error'" elevation="11">
              <h2>Aktueller Kontostand: {{ actualBalanceString }}</h2>
            </v-chip>
          </v-col>
          <v-col>
            <v-btn color="primary" @click="addTransaction">Neue Transaktion</v-btn>
          </v-col>
        </v-row>


        <v-row justify="center">
          <v-col cols="auto">
            <v-text-field outlined filled label="Transaktion suchen"></v-text-field>
          </v-col>
          <v-col cols="auto">
            <v-select
                outlined filled
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
          <v-col cols="auto">
            <v-select
                outlined filled
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
          <v-col cols="auto">
            <date-picker-text-field-range :label="labelDateRange"
                                          :set-local-dates="setDatesFilter"></date-picker-text-field-range>
          </v-col>
        </v-row>
        <transaction-group v-for="transaction in transactions"
                           :id="ref(transaction)"
                           :transaction-prop="transaction"
                           :key="transaction.id"></transaction-group>
      </v-container>
    </v-card>
  </div>
</template>

<script lang="ts">
import {Component, Vue} from "vue-property-decorator";


import TransactionModel from "@/model/TransactionModel";
import TransactionGroup from "@/components/transaction-overview/TransactionGroup.vue";
import {DateTimeFormatter, LocalDate, ZoneId} from "@js-joda/core";
import BankAccountStore from "@/store/modules/BankAccountStore";
import BankAccountModel from "@/model/BankAccountModel";
import TransactionService from "@/service/TransactionService";
import Transaction from "@/components/transaction-overview/Transaction.vue";
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
    TransactionDialog: Transaction,
    TransactionGroup
  }
})
export default class TransactionOverview extends Vue {

  private transactionService: TransactionService = new TransactionService();
  private actualLocalDate: LocalDate = LocalDate.now(ZoneId.of("Europe/Berlin"));
  private selectedCategoriesMember: CategoryModel[] = [];
  private readonly datesDefault: LocalDate[];
  private datesFilterMember: LocalDate[] = [];
  private isLoading: boolean = true;

  private dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");


  created() {
    BankAccountStore.loadBankAccountsIfNotPresent().then(value => {
      let bankAccountId = this.$route.query.bankAccountId;
      let found = false;
      if (bankAccountId === "" || this.bankAccounts.find(value => value.id === bankAccountId) ===
          undefined) {
        this.selectedBankAccount = this.bankAccounts[0];
      }
    });

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
    this.datesDefault = dates.sort((a, b) => a.compareTo(b));
    this.datesFilterMember = this.datesDefault;
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
    return this.transactionService.transactions.filter(
        transaction => {
          if (this.selectedBankAccount !== undefined && transaction.bankAccount !== undefined) {
            return transaction.bankAccount!.id === this.selectedBankAccount!.id;
          }
          else if (transaction.bankAccount === undefined) {
            return true
          }
          else {
            return false;
          }
        }).
    filter(transaction => {
      if (this.selectedCategories.length > 0 && transaction.category !== undefined) {
        return this.selectedCategories.find(
            category => transaction.category!.id === category.id) !==
            undefined;
      }
      else {
        return true;
      }
    }).filter(value => this.isInDateRange(value)).sort((a, b) => b.date.compareTo(a.date));
  }

  get categories(): CategoryModel[] {
    return CategoryStore.categories;
  }

  get labelDateRange(): string {
    if (this.datesDefault[0].isEqual(this.dateFrom) &&
        this.datesDefault[this.datesDefault.length - 1].isEqual(this.dateUntil)) {
      return "alle Daten";
    }
    else {
      return this.dateTimeFormatter.format(this.datesFilter[0]) + " -> " +
          this.dateTimeFormatter.format(this.datesFilter[this.datesFilter.length - 1]);
    }
  }

  get selectedCategories(): CategoryModel[] {
    return this.selectedCategoriesMember;
  }

  set selectedCategories(value: CategoryModel[]) {
    this.selectedCategoriesMember = value;
  }

  get localDates(): LocalDate[] {
    return this.datesDefault;
  }

  get datesFilter(): LocalDate[] {
    return this.datesFilterMember.sort((a, b) => a.compareTo(b));
  }

  setDatesFilter(value: LocalDate[] | undefined) {
    if (value === undefined || value.length === 0) {
      this.datesFilterMember = this.datesDefault;
      return;
    }
    this.datesFilterMember = value!;
  }

  get actualBalanceEuroCent(): number {
    if (this.selectedBankAccount !== undefined) {
      let balance = BalanceStore.balances.find(
          value => value.bankAccountId === this.selectedBankAccount!.id &&
              value.date.isEqual(LocalDate.now()));
      if (balance !== undefined) {
        return balance.balanceEuroCent;
      }
    }
    return 0;
  }

  get actualBalanceString(): string {
    return AmountHelper.euroCentToStringWithEuroSign(this.actualBalanceEuroCent);
  }

  ref(transaction: TransactionModel): string {
    return "ref" + transaction.id!;
  }

  private isInDateRange(value: TransactionModel): boolean {
    return this.datesFilter.find(localDate => localDate.isEqual(value.date)) !== undefined;
  }

  set selectedBankAccount(bankAccount: BankAccountModel | undefined) {
    if (bankAccount !== undefined) {
      this.$router.push("/transactions?bankAccountId=" + bankAccount!.id);
      this.reload();
    }


  }

  get selectedBankAccount(): BankAccountModel | undefined {
    let bankAccountId = this.$route.query.bankAccountId;
    if (bankAccountId !== "") {
      let bankAccountModel = this.bankAccounts.find(
          value => value.id === bankAccountId);
      if (bankAccountModel !== undefined) {
        return bankAccountModel;
      }
    }
    return undefined;
  }


  addTransaction() {
    this.$router.push("/transaction")
  }


  get dateUntil() {
    return this.datesFilter[this.datesFilter.length - 1];
  }

  get dateFrom() {
    return this.datesFilter[0];
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
        this.isLoading = false;
        setTimeout((value: any) => {
          if (this.transactionToScrollTo !== "") {
            this.$vuetify.goTo('#' + this.transactionToScrollTo, {offset: 200})
          }
        }, 100);

      });
    }
    else {
      this.isLoading = false;
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