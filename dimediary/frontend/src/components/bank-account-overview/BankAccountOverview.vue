<template>
  <div>
    <v-row>
      <v-col cols="2">
        <v-select
            :items="bankAccounts"
            item-text="name"
            :value="selectedBankAccount"
            :menu-props="{ maxHeight: '400'}"
            label="Bankkonten"
            multiple
            hint="Wähle die Bankkonten, für die Transaktionen angezeigt werden sollen"
        ></v-select>
      </v-col>
      <v-col cols="1">
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
    <div style="border-style: solid; border-color: #0d47a1; width: 100%; height: 500px">
      This wold become overview of a bank account
    </div>
    <bank-account-dialog v-if="isBankAccountDialog"></bank-account-dialog>
  </div>
</template>

<script lang="ts">


import {Component, Vue} from "vue-property-decorator";
import BankAccountModel from "@/model/BankAccountModel";
import BankAccountStore from "@/store/modules/BankAccountStore";
import BankAccountDialog from "@/components/bank-account-overview/BankAccountDialog.vue";
import DialogStateStore from "@/store/modules/DialogStateStore";

@Component({
  components: {BankAccountDialog}
})
export default class BankAccountOverview extends Vue {

  get bankAccounts(): BankAccountModel[] {
    return BankAccountStore.bankAccounts;
  }

  set selectedBankAccount(bankAccount: BankAccountModel | undefined) {
    BankAccountStore.setBankAccountSelected(bankAccount);
  }

  get selectedBankAccount(): BankAccountModel | undefined {
    return BankAccountStore.bankAccountSelected;
  }

  get isBankAccountDialog(): boolean {
    return DialogStateStore.isBankAccountDialog;
  }

  showDialog() {
    DialogStateStore.setIsBankAccountDialog(true);
  }

}

</script>

<style scoped>

</style>