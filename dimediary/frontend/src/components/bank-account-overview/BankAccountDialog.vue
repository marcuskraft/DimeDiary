<template>
  <v-row justify="center">
    <v-dialog
        v-model="dialog"
        persistent
        max-width="600px"
    >
      <v-card data-ref="bank-account-dialog">
        <v-card-title>
          <span class="headline">Bankkonto anlegen</span>
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
                          label="Name des Kontos*"
                          :rules="[ v => requiredString(v) || 'Name ist ein Pflichtfeld' ]"
                          data-ref="name"
                      ></v-text-field>
                    </v-col>
                  </v-row>
                  <v-row>
                    <v-col data-ref="start-date">
                      <date-picker-text-field
                          :set-local-date="setDateStartBalance"
                          label="Startdatum der Kontoführung*"
                          :rules="[ v => dateStartBalance !== undefined || 'Es muss ein Startdatum angegeben werden' ]"
                      />
                    </v-col>
                  </v-row>
                  <v-row>
                    <v-col>
                      <v-text-field
                          v-model="startBalanceEuroCent"
                          type="number"
                          label="Startguthaben*"
                          suffix="€"
                          :rules="[value => onlyTwoPrecision(value) || 'nur 2 Nachkommastellen möglich' ]"
                          data-ref="start-balance"
                      ></v-text-field>
                    </v-col>
                  </v-row>
                  <v-row>
                    <v-col>
                      <v-select
                          :items="bankAccountsCategories"
                          item-text="name"
                          v-model="selectedBankAccountCategory"
                          label="Kategorie*"
                          return-object
                      ></v-select>
                    </v-col>
                  </v-row>
                </v-col>

                <v-col>
                  <v-row>
                    <v-col>
                      <v-text-field
                          v-model="bankName"
                          type="text"
                          label="Bankname"
                      ></v-text-field>
                    </v-col>
                  </v-row>
                  <v-row>
                    <v-col>
                      <v-text-field
                          v-model="iban"
                          type="text"
                          label="IBAN"
                      ></v-text-field>
                    </v-col>
                  </v-row>
                  <v-row>
                    <v-col>
                      <v-text-field
                          v-model="bic"
                          type="text"
                          label="BIC"
                      ></v-text-field>
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
              data-ref="save"
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
import DialogStateStore from "@/store/modules/DialogStateStore";
import {LocalDate} from "@js-joda/core";
import DatePickerTextField from "@/components/common/DatePickerTextField.vue";
import BankAccountCategoryModel from "@/model/BankAccountCategoryModel";
import BankAccountCategoryStore from "@/store/modules/BankAccountCategoryStore";
import BankAccountModel from "@/model/BankAccountModel";
import BankAccountStore from "@/store/modules/BankAccountStore";
import {onlyTwoPrecision} from "@/helper/amount-utils";

@Component({
  components: {DatePickerTextField}
})
export default class BankAccountDialog extends Vue {

  valid: boolean = true;

  nameMember: string = "";
  dateStartBalance: LocalDate = LocalDate.now();
  startBalanceEuroCentMember: number = 0.0;
  selectedBankAccountCategoryMember: BankAccountCategoryModel = this.bankAccountsCategories[0];
  private _bankName: string = "";
  private _iban: string = "";
  private _bic: string = "";


  requiredString(value: string | undefined): boolean {
    return !(value === undefined || value.length === 0);
  }


  get bankName(): string {
    return this._bankName;
  }

  set bankName(value: string) {
    this._bankName = value;
  }

  get iban(): string {
    return this._iban;
  }

  set iban(value: string) {
    this._iban = value;
  }

  get bic(): string {
    return this._bic;
  }

  set bic(value: string) {
    this._bic = value;
  }

  get name(): string {
    return this.nameMember;
  }

  set name(value: string) {
    this.nameMember = value;
  }

  setDateStartBalance(value: LocalDate) {
    this.dateStartBalance = value;
  }

  get startBalanceEuroCent(): number {
    return this.startBalanceEuroCentMember;
  }

  set startBalanceEuroCent(value: number) {
    this.startBalanceEuroCentMember = value;
  }


  get dialog(): boolean {
    return DialogStateStore.isBankAccountDialog;
  }

  set dialog(value: boolean) {
    DialogStateStore.setIsBankAccountDialog(value);
  }

  get bankAccountsCategories(): BankAccountCategoryModel[] {
    return BankAccountCategoryStore.bankAccountCategories;
  }

  get selectedBankAccountCategory(): BankAccountCategoryModel {
    return this.selectedBankAccountCategoryMember;
  }

  set selectedBankAccountCategory(value: BankAccountCategoryModel) {
    this.selectedBankAccountCategoryMember = value;
  }


  save() {
    let bankAccountModel: BankAccountModel = new BankAccountModel(this.name, this.dateStartBalance,
        this.startBalanceEuroCent * 100, this.selectedBankAccountCategory, undefined, this.bankName,
        this.iban, this.bic);
    BankAccountStore.saveBankAccount(bankAccountModel);
    this.dialog = false;
  }

  onlyTwoPrecision(value: number): boolean {
    return onlyTwoPrecision(value);
  }

}

</script>

<style scoped>

</style>