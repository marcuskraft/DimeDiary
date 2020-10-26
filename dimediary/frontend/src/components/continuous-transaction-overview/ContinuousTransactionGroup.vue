<template>
  <v-hover v-slot:default="{ hover }">
    <v-card class="transaction-group" max-width="80%" outlined
            :elevation="hover ? 12 : 2"
            rounded @click="openContinuousTransaction">
      <v-container>
        <v-row>
          <v-col cols="auto">
            <div>
              <b>{{ dateString }}</b>
            </div>
          </v-col>
          <v-col cols="auto">
            <v-row>
              <b>{{ name }}</b>
            </v-row>
            <v-row>
              <small v-if="category !== undefined">{{ category.name }}</small>
            </v-row>
          </v-col>
          <v-spacer></v-spacer>
          <v-col cols="2">
            <v-row>
              <b>{{ amount }}</b>
            </v-row>
            <v-row>
              <small>{{ balance }}</small>
            </v-row>
          </v-col>
        </v-row>
      </v-container>
    </v-card>
  </v-hover>
</template>

<script lang="ts">
import {Component, Prop, Vue} from "vue-property-decorator";
import {DateTimeFormatter} from "@js-joda/core";
import DatePickerTextField from "@/components/common/DatePickerTextField.vue";
import CategoryModel from "@/model/CategoryModel";
import BalanceStore from "@/store/modules/BalanceStore";
import AmountHelper from "@/helper/AmountHelper";
import ContinuousTransactionModel from "../../model/ContinuousTransactionModel";

require('@js-joda/timezone');

const {
  Locale,
} = require("@js-joda/locale_de-de")

@Component({
  components: {DatePickerTextField}
})
export default class ContinuousTransactionGroup extends Vue {

  @Prop({
    type: ContinuousTransactionModel,
    required: true
  }) continuousTransactionProp!: ContinuousTransactionModel;

  private dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");


  get dateString(): string {
    return this.dateTimeFormatter.format(this.continuousTransactionProp.dateBegin);
  }

  get name(): string {
    return this.continuousTransactionProp.name;
  }

  get amount(): string {
    return AmountHelper.euroCentToStringWithEuroSign(this.continuousTransactionProp.amountEuroCent);
  }


  get category(): CategoryModel | undefined {
    return this.continuousTransactionProp.category;
  }

  get balance(): string {
    let balanceFind = BalanceStore.balances.find(
        balance => balance.date.equals(this.continuousTransactionProp.dateBegin) &&
            balance.bankAccountId ===
            this.continuousTransactionProp.bankAccount!.id);
    if (balanceFind !== undefined) {
      return AmountHelper.euroCentToStringWithEuroSign(balanceFind.balanceEuroCent);
    }
    return "";
  }

  openContinuousTransaction() {
    this.$router.push("/continuous-transaction/" + this.continuousTransactionProp.id);
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