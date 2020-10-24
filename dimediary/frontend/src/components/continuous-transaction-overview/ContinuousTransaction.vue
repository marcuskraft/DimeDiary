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
                  <date-picker-text-field
                      :set-local-date="setLocalDate"
                      label="Datum*"
                      :local-date="dateMember"
                      :rules="[ v => dateMember !== undefined || 'Pflichtfeld' ]"/>
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
import AmountHelper from "@/helper/AmountHelper";
import BankAccountStore from "@/store/modules/BankAccountStore";
import CategoryStore from "@/store/modules/CategoryStore";
import TransactionService from "@/service/TransactionService";
import ContinuousTransactionStore from "../../store/modules/ContinuousTransactionStore";
import RecurrenceSettingsModel from "@/model/RecurrenceSettingsModel";

@Component({
  components: {DatePickerTextField}
})
export default class ContinuousTransaction extends Vue {

  @Prop({type: String}) continuousTransactionId?: string;

  private dialog: boolean = false;

  nameMember: string;
  dateMember: LocalDate;
  amountEuroCentMember: number;
  fixCostMember: boolean;
  bankAccountMember?: BankAccountModel;
  categoryMember?: CategoryModel;
  id?: string;

  continuousTransaction: ContinuousTransactionModel | undefined;

  valid: boolean = true;

  loading: boolean = true;

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

  private loadContinuousTransaction(continuousTransactionId: string) {
    ContinuousTransactionStore.loadContinuousTransaction(continuousTransactionId).then(transaction => {
      this.id = transaction.id;
      this.nameMember = transaction.name;
      this.dateMember = LocalDate.from(transaction.dateBegin);
      this.amountEuroCentMember = transaction.amountEuroCent / 100;
      this.fixCostMember = transaction.fixCost;
      this.bankAccountMember = transaction.bankAccount;
      this.categoryMember = transaction.category;
      this.continuousTransaction = transaction;
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
  }

  get categories(): CategoryModel[] {
    return CategoryStore.categories;
  }

  get bankAccounts(): BankAccountModel[] {
    return BankAccountStore.bankAccounts;
  }

  get dialogTitle(): string {
    return this.continuousTransaction !==
    undefined ? "Transaktionsreihe bearbeten" : "Transaktionsreihe anlegen";
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


  requiredString(value: string | undefined): boolean {
    return !(value === undefined || value.length === 0);
  }

  setLocalDate(localDate: LocalDate) {
    this.dateMember = localDate;
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
      this.continuousTransaction.dateBegin = this.date;
      continuousTransaction = this.continuousTransaction;
    }
    else {
      continuousTransaction = new ContinuousTransactionModel(this.name, this.amountEuroCent,this.date,this.bankAccount!,this.category!,this.recurrenceSettingsModel(),this.fixCost);
    }
    let transactionService: TransactionService = new TransactionService();
    ContinuousTransactionStore.saveContinuousTransaction(continuousTransaction);
    this.close();
  }

  get recurrenceSettingsModel() {
    return new RecurrenceSettingsModel();
  }

  deleteTransaction() {
    if (this.continuousTransaction !== undefined) {
      let transactionService: TransactionService = new TransactionService();
      transactionService.deleteTransaction(this.continuousTransaction).then(value => this.close());
    }
  }

}


</script>

<style scoped>

</style>