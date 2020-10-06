<template>
  <v-row justify="center">
    <v-dialog
        v-model="dialog"
        persistent
        max-width="600px"
    >
      <v-card>
        <v-card-title>
          <span class="headline">{{ dialogTitle }}</span>
        </v-card-title>
        <v-card-text>
          <v-form v-model="valid">
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
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
              color="blue darken-1"
              text
              @click="closeDialog"
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
    </v-dialog>
  </v-row>
</template>

<script lang="ts">
import {Component, Vue} from "vue-property-decorator";
import TransactionModel from "@/model/TransactionModel";
import {LocalDate} from "@js-joda/core";
import BankAccountModel from "@/model/BankAccountModel";
import CategoryModel from "@/model/CategoryModel";
import ContinuousTransactionModel from "@/model/ContinuousTransactionModel";
import DatePickerTextField from "@/components/common/DatePickerTextField.vue";
import AmountHelper from "@/helper/AmountHelper";
import BankAccountStore from "@/store/modules/BankAccountStore";
import CategoryStore from "@/store/modules/CategoryStore";
import DialogStateStore from "@/store/modules/DialogStateStore";
import TransactionService from "@/service/TransactionService";
import TransactionStore from "@/store/modules/TransactionStore";

@Component({
  components: {DatePickerTextField}
})
export default class TransactionDialog extends Vue {


  nameMember: string;
  dateMember: LocalDate;
  amountEuroCentMember: number;
  fixCostMember: boolean;
  bankAccountMember?: BankAccountModel;
  categoryMember?: CategoryModel;
  continuousTransactionMember?: ContinuousTransactionModel;

  valid: boolean = true;


  constructor() {
    super();
    this.nameMember = this.selectedTransaction !== undefined ? this.selectedTransaction.name : "";
    this.dateMember =
        this.selectedTransaction !== undefined ? this.selectedTransaction.date : LocalDate.now();
    this.amountEuroCentMember =
        this.selectedTransaction !== undefined ? this.selectedTransaction.amountEuroCent /
            100 : 0.0;
    this.fixCostMember =
        this.selectedTransaction !== undefined ? this.selectedTransaction.fixCost : false;
    this.bankAccountMember =
        this.selectedTransaction !==
        undefined ? this.selectedTransaction.bankAccount : this.bankAccounts[0];
    this.categoryMember =
        this.selectedTransaction !==
        undefined ? this.selectedTransaction.category : this.categories[0];
    this.continuousTransactionMember =
        this.selectedTransaction !==
        undefined ? this.selectedTransaction.continuousTransaction : undefined;
  }

  get selectedTransaction(): TransactionModel | undefined {
    return TransactionStore.selectedTransaction;
  }

  get categories(): CategoryModel[] {
    return CategoryStore.categories;
  }

  get bankAccounts(): BankAccountModel[] {
    return BankAccountStore.bankAccounts;
  }

  get dialogTitle(): string {
    return this.selectedTransaction !== undefined ? "Transaktion bearbeten" : "Transaktion anlegen";
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
    return AmountHelper.onlyTwoPrecision(value);
  }

  get dialog(): boolean {
    return DialogStateStore.isTransactionDialog;
  }

  closeDialog() {
    DialogStateStore.setIsTransactionDialog(false);
    TransactionStore.setSelectedTransaction(undefined);
  }


  save() {
    let transaction: TransactionModel;
    if (this.selectedTransaction !== undefined) {
      this.selectedTransaction.name = this.name;
      this.selectedTransaction.continuousTransaction = this.continuousTransaction;
      this.selectedTransaction.category = this.category;
      this.selectedTransaction.bankAccount = this.bankAccount;
      this.selectedTransaction.fixCost = this.fixCost;
      this.selectedTransaction.amountEuroCent = this.amountEuroCent * 100;
      this.selectedTransaction.date = this.date;
      transaction = this.selectedTransaction;
    }
    else {
      transaction = new TransactionModel(this.name, this.date, this.amountEuroCent * 100,
          this.fixCost, this.bankAccount, this.category, this.continuousTransaction, undefined);
    }
    let transactionService: TransactionService = new TransactionService();
    transactionService.saveTransaction(transaction);
    this.closeDialog();
  }

}

</script>

<style scoped>

</style>