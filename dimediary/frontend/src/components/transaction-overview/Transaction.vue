<template>
  <v-form v-model="valid" data-ref="transaction-page">
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
                      data-ref="name"
                  ></v-text-field>
                </v-col>
              </v-row>
              <v-row>
                <v-col data-ref="date">
                  <date-picker-text-field
                      :set-local-date="setLocalDate"
                      label="Datum*"
                      :local-date="dateMember"
                      :rules="[ v => dateMember !== undefined || 'Pflichtfeld' ]"
                  />
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
                      data-ref="amount"
                  ></v-text-field>
                </v-col>
              </v-row>
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
            </v-col>

            <v-col>
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
              <v-row>
                <v-col>
                  <v-checkbox v-model="fixCost" label="Fixkosten"/>
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
            data-ref="save"
        >
          OK
        </v-btn>
      </v-card-actions>
    </v-card>
    <v-dialog
        v-model="dialog"
        width="500"
    >
      <v-card>
        <v-card-title class="headline grey lighten-2">
          Transaktion löschen
        </v-card-title>


        <v-alert
            border="bottom"
            dense
            type="warning"
            elevation="2">
          Soll die Transaktion wirklich gelöscht werden?
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
              @click="deleteTransaction">
            OK
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-form>
</template>

<script lang="ts">
import {Component, Prop, Vue} from "vue-property-decorator";
import TransactionModel from "@/model/TransactionModel";
import {LocalDate} from "@js-joda/core";
import BankAccountModel from "@/model/BankAccountModel";
import CategoryModel from "@/model/CategoryModel";
import ContinuousTransactionModel from "@/model/ContinuousTransactionModel";
import DatePickerTextField from "@/components/common/DatePickerTextField.vue";
import BankAccountStore from "@/store/modules/BankAccountStore";
import CategoryStore from "@/store/modules/CategoryStore";
import TransactionService from "@/service/TransactionService";
import {onlyTwoPrecision} from "@/helper/amount-utils";

@Component({
  components: {DatePickerTextField}
})
export default class Transaction extends Vue {

  @Prop({type: String}) transactionId?: string;

  private dialog: boolean = false;

  nameMember: string;
  dateMember: LocalDate;
  amountEuroCentMember: number;
  fixCostMember: boolean;
  bankAccountMember?: BankAccountModel;
  categoryMember?: CategoryModel;
  continuousTransactionMember?: ContinuousTransactionModel;
  id?: string;

  transaction: TransactionModel | undefined;

  valid: boolean = true;

  loading: boolean = true;

  created() {
    BankAccountStore.loadBankAccountsIfNotPresent();
    CategoryStore.loadCategoriesIfNotPresent();
    if (this.transactionId !== undefined) {
      this.loadTransaction(this.transactionId);
    } else {
      this.loading = false;
    }
  }

  private loadTransaction(transactionId: string) {
    let transactionService: TransactionService = new TransactionService();
    transactionService.loadTransaction(transactionId).then(transaction => {
      this.id = transaction.id;
      this.transaction = transaction;
      this.nameMember = transaction.name;
      this.dateMember = LocalDate.from(transaction.date);
      this.amountEuroCentMember = transaction.amountEuroCent / 100;
      this.fixCostMember = transaction.fixCost;
      this.bankAccountMember = transaction.bankAccount;
      this.categoryMember = transaction.category;
      this.continuousTransactionMember = transaction.continuousTransaction;
      this.loading = false;
    });
  }


  constructor() {
    super();
    this.nameMember = "";
    this.dateMember = LocalDate.now();
    this.amountEuroCentMember = 0.0;
    this.fixCostMember = false;
    this.bankAccountMember = this.bankAccounts[0];
    this.categoryMember = this.categories[0];
    this.continuousTransactionMember = undefined;
  }

  get categories(): CategoryModel[] {
    return CategoryStore.categories;
  }

  get bankAccounts(): BankAccountModel[] {
    return BankAccountStore.bankAccounts;
  }

  get dialogTitle(): string {
    return this.transaction !== undefined ? "Transaktion bearbeten" : "Transaktion anlegen";
  }

  get name(): string {
    return this.nameMember;
  }

  set name(value: string) {
    this.nameMember = value;
  }

  get date(): LocalDate {
    return this.dateMember;
  }

  set date(value: LocalDate) {
    this.dateMember = value;
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

  get continuousTransaction(): ContinuousTransactionModel | undefined {
    return this.continuousTransactionMember;
  }

  set continuousTransaction(value: ContinuousTransactionModel | undefined) {
    this.continuousTransactionMember = value;
  }

  requiredString(value: string | undefined): boolean {
    return !(value === undefined || value.length === 0);
  }

  setLocalDate(localDate: LocalDate) {
    this.dateMember = localDate;
  }

  onlyTwoPrecision(value: number): boolean {
    return onlyTwoPrecision(value);
  }


  close() {
    this.$router.back();
  }


  save() {
    let transaction: TransactionModel;
    if (this.transaction !== undefined) {
      this.transaction.name = this.name;
      this.transaction.continuousTransaction = this.continuousTransaction;
      this.transaction.category = this.category;
      this.transaction.bankAccount = this.bankAccount;
      this.transaction.fixCost = this.fixCost;
      this.transaction.amountEuroCent = this.amountEuroCent * 100;
      this.transaction.date = this.date;
      transaction = this.transaction;
    } else {
      transaction = new TransactionModel(this.name, this.date, this.amountEuroCent * 100,
          this.fixCost, this.bankAccount, this.category, this.continuousTransaction, this.id);
    }
    let transactionService: TransactionService = new TransactionService();
    transactionService.saveTransaction(transaction);
    this.close();
  }

  deleteTransaction() {
    if (this.transaction !== undefined) {
      let transactionService: TransactionService = new TransactionService();
      transactionService.deleteTransaction(this.transaction).then(value => this.close());
    }
  }

}

</script>

<style scoped>

</style>