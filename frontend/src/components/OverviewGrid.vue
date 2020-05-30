<template>
  <v-container class="grey lighten-5">
    <v-row class="grey lighten-5">
      <v-col cols="1">
        <v-label>Datum</v-label>
      </v-col>
      <v-col cols="1">
        <v-label>Wochentag</v-label>
      </v-col>
      <v-col cols="1">
        <v-label>Kontostand</v-label>
      </v-col>
      <v-col cols="9">
        <v-label>Transaktionen</v-label>
      </v-col>
    </v-row>

    <v-row class="grey lighten-5" v-for="(date,i) in dates" :key="i">
      <v-col cols="1">
        <v-label>{{ formatDate(date) }}</v-label>
      </v-col>
      <v-col cols="1">
        <v-label>{{ dayOfWeek(date) }}</v-label>
      </v-col>
      <v-col cols="1">
        <v-label>{{ balanceOfDate(date) }}</v-label>
      </v-col>
      <v-col cols="9">
        <TransactionSlideGroup :transactions-prop="transactionOfDate(date)"></TransactionSlideGroup>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { LocalDate, DateTimeFormatter } from "@js-joda/core";
import TransactionModel from "../model/TransactionModel";
import TransactionModelArray from "../model/TransactionModelArray";
import TransactionSlideGroup from "./TransactionSlideGroup.vue";

@Component({
  components: {
    TransactionSlideGroup
  }
})
export default class OverviewGrid extends Vue {
  private dates: LocalDate[];

  private transactionMap: Map<string, TransactionModelArray>;

  private balanceMap: Map<string, number>;

  private dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(
    "dd.MM.yyyy"
  );

  constructor() {
    super();
    this.dates = [];
    this.transactionMap = new Map();
    this.balanceMap = new Map();
    for (let i = 1; i <= 31; i++) {
      let date: LocalDate = LocalDate.of(2020, 1, i);
      this.dates.push(date);

      let transaction: TransactionModel = new TransactionModel(
        "das ist " + i,
        date,
        i
      );
      let transactionList: TransactionModelArray = new TransactionModelArray(
        []
      );
      transactionList.addTransaction(transaction);

      this.transactionMap.set(date.format(this.dateFormatter), transactionList);

      this.balanceMap.set(date.format(this.dateFormatter), i);
    }
  }

  formatDate(date: LocalDate): string {
    return date.format(this.dateFormatter);
  }

  dayOfWeek(date: LocalDate): string {
    return date.dayOfWeek().toString();
  }

  balanceOfDate(date: LocalDate): number | undefined {
    return this.balanceMap.get(date.format(this.dateFormatter));
  }

  transactionOfDate(date: LocalDate): TransactionModelArray | undefined {
    return this.transactionMap.get(date.format(this.dateFormatter));
  }
}
</script>

<style scoped>
</style>
