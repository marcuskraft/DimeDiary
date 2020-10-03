<template>
  <v-row justify="center">
    <v-dialog
        v-model="dialog"
        persistent
        max-width="600px"
    >
      <v-card>
        <v-card-title>
          <span class="headline">Bankkonto</span>
        </v-card-title>
        <v-card-text>
          <v-form v-model="valid">
            <v-container>
              <v-row>
                <v-col
                    cols="12"
                    md="4"
                >
                  <v-text-field
                      v-model="name"
                      type="text"
                      label="Name"
                      :rules="[ v => requiredString(v) || 'Name ist ein Pflichtfeld' ]"
                  ></v-text-field>
                </v-col>
              </v-row>
              <v-row>
                <v-col
                    cols="12"
                    md="4"
                >
                  <date-picker-text-field
                      :set-local-date="setDateStartBalance"></date-picker-text-field>
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
import {Component, Vue} from "vue-property-decorator";
import DialogStateStore from "@/store/modules/DialogStateStore";
import {LocalDate} from "@js-joda/core";
import DatePickerTextField from "@/components/common/DatePickerTextField.vue";

@Component({
  components: {DatePickerTextField}
})
export default class BankAccountDialog extends Vue {

  private valid: boolean = true;

  private nameMember: string = "";
  private dateStartBalance: LocalDate = LocalDate.now();
  private startBalanceEuroCentMember: number = 0.0;

  requiredString(value: string | undefined): boolean {
    return !(value === undefined || value.length === 0);
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


  save() {
    this.dialog = false;
  }

}

</script>

<style scoped>

</style>