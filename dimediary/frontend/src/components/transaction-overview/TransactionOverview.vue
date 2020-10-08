<template>
  <div :key="transactions.length">
    <v-row>
      <v-col cols="1">
        <v-select
            :items="bankAccounts"
            item-text="name"
            v-model="selectedBankAccount"
            :menu-props="{ maxHeight: '400'}"
            label="Bankkonten"
            return-object
            hint="Wähle die Bankkonten, für die Transaktionen angezeigt werden sollen"
            @change="loadTransactions"
        ></v-select>
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
    <transaction-group v-for="transaction in transactions" :id="ref(transaction)"
                       :transaction-prop="transaction" :key="transaction.id"></transaction-group>
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

  private actualLocalDate: LocalDate = LocalDate.now(ZoneId.of("Europe/Berlin"))

  created() {
    this.loadTransactions();
  }

  mounted() {

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

  get localDates(): LocalDate[] {
    let dates: LocalDate[] = [];
    for (let i = 0; i < 31; i++) {
      dates.push(this.actualLocalDate.plusDays(i));
    }
    for (let i = 1; i < 30; i++) {
      dates.push(this.actualLocalDate.minusDays(i));
    }
    return dates.sort((a, b) => a.compareTo(b));
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


  private loadTransactions() {
    if (this.selectedBankAccount !== undefined) {
      this.transactionService.loadTransactions(this.selectedBankAccount.id!,
          this.localDates[0],
          this.localDates[this.localDates.length - 1]).
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

</style>