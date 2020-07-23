<template>
  <div>
    <v-row>
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

    <v-row v-for="(date,i) in dates" :key="i">
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
  </div>
</template>

<script lang="ts">
  import {Component, Vue} from "vue-property-decorator";
  import {DateTimeFormatter, LocalDate} from "@js-joda/core";
  import TransactionSlideGroup from "@/components/overview/TransactionSlideGroup.vue";
  import TransactionStore from "@/store/modules/TransactionStore";
  import DayTransactions, {DayTransactionsArray} from "@/model/DayTransactions";

  @Component({
    components: {
      TransactionSlideGroup
    }
  })
  export default class OverviewGrid extends Vue {

    //@Prop({type: DayTransactionsArray, required: true}) dayTransactionsArray!: DayTransactionsArray;


    private balanceMap: Map<string, number>;

    private dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(
        "dd.MM.yyyy"
    );

    constructor() {
      super();

      this.balanceMap = new Map();

      let i: number = 0;
      this.dates.forEach(date => {
        this.balanceMap.set(date.format(this.dateFormatter), ++i);
      });


    }

    get dates(): LocalDate[] {
      return this.dayTransactionsArray.dayTransactions.map(value => value.date).sort((a: LocalDate, b: LocalDate) => {
        return a.compareTo(b);
      })
    }

    get dayTransactionsArray(): DayTransactionsArray {
      return TransactionStore.transactions;
    }

    get key(): number {
      let sum: number = 0;
      this.dayTransactionsArray.dayTransactions.map(value => value.transactions.length).forEach(value => sum + value);
      return sum;
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

    transactionOfDate(date: LocalDate): DayTransactions {
      let dayTransactions: DayTransactions | undefined = this.dayTransactionsArray.getDayTransaction(date);
      if (dayTransactions == undefined) {
        dayTransactions = new DayTransactions(date, []);
        TransactionStore.addDayTransaction(dayTransactions);
      }
      return dayTransactions;
    }
  }
</script>

<style scoped>
</style>
