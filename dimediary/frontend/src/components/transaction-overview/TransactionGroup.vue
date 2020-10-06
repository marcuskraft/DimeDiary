<template>
  <div class="transaction-group">
    <v-row style="margin: 5px">
      <v-col cols="1">
        <v-row>
          <v-col>
            <v-btn @click="showDialog">
              <v-icon>edit</v-icon>
            </v-btn>
          </v-col>
          <v-col>
            <v-btn @click="dialog=true">
              <v-icon>delete</v-icon>
            </v-btn>
          </v-col>
        </v-row>

      </v-col>
      <v-col cols="1">
        <date-picker-text-field :set-local-date="setLocalDate"
                                :local-date="date"
                                :inTransactionGroup=true
                                :key="date.toString()"></date-picker-text-field>
      </v-col>
      <v-col cols="1">
        <v-text-field
            v-model="name"
            type="text"
            filled
            outlined
            @change="save"
            label="Bezeichnung"
        ></v-text-field>
      </v-col>
      <v-col cols="1">
        <v-form>
          <v-text-field
              v-model="amount"
              type="number"
              filled
              outlined
              suffix="€"
              label="Betrag"
              @change="save"
              :rules="[value => onlyTwoPrecision(value) || 'nur 2 Nachkommastellen möglich' ]"
          ></v-text-field>
        </v-form>
      </v-col>
      <v-col cols="1">
        <v-select
            outlined
            filled
            :items="categories"
            item-text="name"
            v-model="category"
            label="Kategorie"
            return-object
            @change="save"
        />
      </v-col>
      <v-col cols="1">
        <v-select
            outlined
            filled
            :items="bankAccounts"
            item-text="name"
            v-model="bankAccount"
            label="Konto"
            return-object
            @change="save"
        />
      </v-col>
    </v-row>
    <v-dialog
        v-model="dialog"
        persistent
        max-width="600px"
    >
      <v-card>
        <v-card-title>
          <span class="headline">Transaktion löschen?</span>
        </v-card-title>
        <v-card-text>
          <v-alert
              outlined
              type="warning"
              prominent
              border="left"
          >
            Sicher, dass Sie diese Transaktion löschen wollen?
          </v-alert>
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
              @click="deleteTransaction"
          >
            OK
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script lang="ts">
import {Component, Prop, Vue} from "vue-property-decorator";
import TransactionModel from "@/model/TransactionModel";
import {LocalDate} from "@js-joda/core";
import TransactionService from "@/service/TransactionService";
import DatePickerTextField from "@/components/common/DatePickerTextField.vue";
import AmountHelper from "@/helper/AmountHelper";
import TransactionStore from "@/store/modules/TransactionStore";
import DialogStateStore from "@/store/modules/DialogStateStore";
import CategoryModel from "@/model/CategoryModel";
import CategoryStore from "@/store/modules/CategoryStore";
import BankAccountModel from "@/model/BankAccountModel";
import BankAccountStore from "@/store/modules/BankAccountStore";

@Component({
  components: {DatePickerTextField}
})
export default class TransactionGroup extends Vue {

  @Prop({type: TransactionModel, required: true}) transactionProp!: TransactionModel;


  private dialog: boolean = false;

  get date(): LocalDate {
    return this.transactionProp.date;
  }

  set date(localDate: LocalDate) {
    this.transactionProp.date = localDate;
  }

  setLocalDate(localDate: LocalDate) {
    this.transactionProp.date = localDate;
    this.save();
  }

  get bankAccount(): BankAccountModel | undefined {
    return this.transactionProp.bankAccount;
  }

  set bankAccount(value: BankAccountModel | undefined) {
    this.transactionProp.bankAccount = value;
  }

  get name(): string {
    return this.transactionProp.name;
  }

  set name(value: string) {
    this.transactionProp.name = value;
  }

  get amount(): number {
    return this.transactionProp.amountEuroCent / 100;
  }

  set amount(amount: number) {
    this.transactionProp.amountEuroCent = amount * 100;
  }

  get categories(): CategoryModel[] {
    return CategoryStore.categories;
  }

  get bankAccounts(): BankAccountModel[] {
    return BankAccountStore.bankAccounts;
  }

  get category(): CategoryModel | undefined {
    return this.transactionProp.category;
  }

  set category(value: CategoryModel | undefined) {
    this.transactionProp.category = value;
  }

  onlyTwoPrecision(value: number): boolean {
    return AmountHelper.onlyTwoPrecision(value);
  }


  save() {
    if (this.onlyTwoPrecision(this.amount)) {
      let transactionService: TransactionService = new TransactionService();
      transactionService.saveTransaction(this.transactionProp);
      console.info("saved");
    }
  }

  deleteTransaction() {
    let transactionService: TransactionService = new TransactionService();
    transactionService.deleteTransaction(this.transactionProp);
    this.dialog = false;
    console.info("deleted");
  }


  showDialog() {
    TransactionStore.setSelectedTransaction(this.transactionProp);
    DialogStateStore.setIsTransactionDialog(true);
  }


}
</script>

<style scoped>

.transaction-group {
  background-color: #00b0ff;
  margin: 3px;
}

::v-deep input::-webkit-outer-spin-button,
::v-deep input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

</style>