<template>
  <v-card class="transaction-group" max-width="50%" min-width="400px" outlined elevation="2"
          rounded>
    <v-row>
      <v-col cols="2">
        <div>
          <b>{{ dateString }}</b>
        </div>
      </v-col>
      <v-col cols="8">
        <v-row>
          <b>{{ name }}</b>
        </v-row>
        <v-row>
          <small v-if="category !== undefined">{{ category.name }}</small>
        </v-row>
      </v-col>
      <v-col cols="2">
        <v-row>
          <b>{{ amount }}</b>
        </v-row>
        <v-row>
          <small>{{ balance }}</small>
        </v-row>
      </v-col>
    </v-row>
  </v-card>
</template>

<script lang="ts">
import {Component, Prop, Vue} from "vue-property-decorator";
import TransactionModel from "@/model/TransactionModel";
import {DateTimeFormatter} from "@js-joda/core";
import DatePickerTextField from "@/components/common/DatePickerTextField.vue";
import CategoryModel from "@/model/CategoryModel";
import BalanceStore from "@/store/modules/BalanceStore";
import AmountHelper from "@/helper/AmountHelper";

require('@js-joda/timezone');

const {
  Locale,
} = require("@js-joda/locale_de-de")

@Component({
  components: {DatePickerTextField}
})
export default class TransactionGroup extends Vue {

  @Prop({type: TransactionModel, required: true}) transactionProp!: TransactionModel;

  private dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");


  get dateString(): string {
    return this.dateTimeFormatter.format(this.transactionProp.date);
  }

  get name(): string {
    return this.transactionProp.name;
  }

  get amount(): string {
    return AmountHelper.euroCentToStringWithEuroSign(this.transactionProp.amountEuroCent);
  }


  get category(): CategoryModel | undefined {
    return this.transactionProp.category;
  }

  get balance(): string {
    let balanceFind = BalanceStore.balances.find(
        balance => balance.date.equals(this.transactionProp.date) && balance.bankAccountId ===
            this.transactionProp.bankAccount!.id);
    if (balanceFind !== undefined) {
      return AmountHelper.euroCentToStringWithEuroSign(balanceFind.balanceEuroCent);
    }
    return "";
  }


}
</script>

<style scoped>

.transaction-group {
  margin-left: auto;
  margin-right: auto;
  margin-top: 5px;
}


</style>