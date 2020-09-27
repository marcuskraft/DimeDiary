<template>
  <div>
    <v-container fluid>
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
        <v-col cols="9" style="padding: 3px">
          <TransactionSlideGroup
              :transactions-prop="transactionOfDate(date)"></TransactionSlideGroup>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script lang="ts">
import {Component, Vue} from "vue-property-decorator";
import {DateTimeFormatter, LocalDate} from "@js-joda/core";
import TransactionSlideGroup from "@/components/overview/TransactionSlideGroup.vue";
import TransactionStore from "@/store/modules/TransactionStore";
import DayTransactions, {DayTransactionsArray} from "@/model/DayTransactions";
import TimeService from "@/helper/TimeService";
import OverviewNavigationStore from "@/store/modules/OverviewNavigationStore";

@Component({
  components: {
    TransactionSlideGroup
  }
})
export default class OverviewGrid extends Vue {

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
    return TimeService.getDatesFor(OverviewNavigationStore.year, OverviewNavigationStore.month)
  }

  get dayTransactionsArray(): DayTransactionsArray {
    return TransactionStore.transactions;
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
    let dayTransactions: DayTransactions | undefined = this.dayTransactionsArray.getDayTransaction(
        date);
    if (dayTransactions == undefined) {
      dayTransactions = new DayTransactions(date, []);
      TransactionStore.addDayTransaction(dayTransactions);
    }
    return dayTransactions;
  }
}
</script>

<style scoped>
.row {
  height: 40px;
  border: 1px dotted dimgray;
}

.col {
  border-right: 1px dotted dimgray;;
}

</style>
