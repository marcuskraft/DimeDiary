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
        <v-slide-group multiple show-arrows>
          <v-slide-item v-for="n in 15" :key="n" v-slot:default="{ active, toggle }">
            <v-btn
              :input-value="active"
              active-class="purple white--text"
              depressed
              rounded
              @click="toggle"
            >Options {{ n }}</v-btn>
          </v-slide-item>
        </v-slide-group>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { LocalDate, DateTimeFormatter } from "@js-joda/core";

@Component
export default class OverviewGrid extends Vue {
  private dates: LocalDate[];

  private transactionMap: Map<string, string>;

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

      this.transactionMap.set(
        date.format(this.dateFormatter),
        "This is transaction " + i
      );

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

  transactionOfDate(date: LocalDate): string | undefined {
    return this.transactionMap.get(date.format(this.dateFormatter));
  }
}
</script>

<style scoped>
</style>
