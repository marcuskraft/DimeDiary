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
              @click="dialog = false"
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
import DialogStateStore from "@/store/modules/DialogStateStore";
import TransactionService from "@/service/TransactionService";

@Component({
  components: {DatePickerTextField}
})
export default class TransactionDialog extends Vue {

  @Prop({type: TransactionModel}) private transactionProp?: TransactionModel;

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
    this.nameMember = this.transactionProp !== undefined ? this.transactionProp.name : "";
    this.dateMember =
        this.transactionProp !== undefined ? this.transactionProp.date : LocalDate.now();
    this.amountEuroCentMember =
        this.transactionProp !== undefined ? this.transactionProp.amountEuroCent : 0.0;
    this.fixCostMember = this.transactionProp !== undefined ? this.transactionProp.fixCost : false;
    this.bankAccountMember =
        this.transactionProp !== undefined ? this.transactionProp.bankAccount : undefined;
    this.categoryMember =
        this.transactionProp !== undefined ? this.transactionProp.category : undefined;
    this.continuousTransactionMember =
        this.transactionProp !== undefined ? this.transactionProp.continuousTransaction : undefined;
  }

  get categories(): CategoryModel[] {
    return CategoryStore.categories;
  }

  get bankAccounts(): BankAccountModel[] {
    return BankAccountStore.bankAccounts;
  }

  get dialogTitle(): string {
    return this.transactionProp !== undefined ? "Transaktion bearbeten" : "Transaktion anlegen";
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

  set dialog(value: boolean) {
    DialogStateStore.setIsTransactionDialog(value);
  }


  save() {
    let transaction: TransactionModel;
    if (this.transactionProp !== undefined) {
      this.transactionProp.name = this.name;
      this.transactionProp.continuousTransaction = this.continuousTransaction;
      this.transactionProp.category = this.category;
      this.transactionProp.bankAccount = this.bankAccount;
      this.transactionProp.fixCost = this.fixCost;
      this.transactionProp.amountEuroCent = this.amountEuroCent;
      this.transactionProp.date = this.date;
      transaction = this.transactionProp;
    }
    else {
      transaction = new TransactionModel(this.name, this.date, this.amountEuroCent * 100,
          this.fixCost, this.bankAccount, this.category, this.continuousTransaction, undefined);
    }
    let transactionService: TransactionService = new TransactionService();
    transactionService.saveTransaction(transaction);
    this.dialog = false;
  }

}

</script>

<style scoped>

</style>