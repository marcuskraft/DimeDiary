<template>
  <div class="grid">
    <!-- Headers -->
    <v-label>Datum</v-label>
    <v-label>Wochentag</v-label>
    <v-label>Kontostand</v-label>
    <div v-for="m in 1" :key="m"></div>

    <template v-for="date in dates">
      <div :key="'date' + formatDate(date)">{{ formatDate(date) }}</div>
      <div :key="'day' + formatDate(date)">{{ dayOfWeek(date) }}</div>
      <div :key=" 'balance' + formatDate(date)">{{ balanceOfDate(date) }}</div>

      <v-chip
        class="ma-2"
        color="blue"
        :key="'transaction' + formatDate(date)"
      >{{transactionOfDate(date)}}</v-chip>
    </template>
  </div>
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
        "This is transaction {{i}}"
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
.grid {
  display: grid;
  grid-template-columns: repeat(4, 150px);
  grid-template-rows: repeat(32, auto);
  text-align: center;
  overflow: auto;
  white-space: nowrap;
}
</style>
