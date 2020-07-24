<template>
  <div>
    <v-container style="height: 80px" fluid>
      <v-row justify="center">
        <v-col cols="1">
          <v-text-field
              label="Jahr"
              v-model.number="year"
              type="number"/>
        </v-col>
        <v-col cols="1">
          <v-select
              clear-icon="text-capitalize"
              label="Monat"
              :items="allMonths"
              v-model="month"/>
        </v-col>
      </v-row>
    </v-container>
    <overview-grid></overview-grid>
  </div>
</template>

<script lang="ts">
  import {Component, Vue} from "vue-property-decorator";
  import OverviewNavigationStore from "@/store/modules/OverviewNavigationStore";
  import TimeService from "@/helper/TimeService";
  import {TransactionGetRequestImpl} from "@/services/TransactionService";
  import TransactionStore from "@/store/modules/TransactionStore";
  import {DateTimeFormatter, LocalDate, Month} from "@js-joda/core";
  import OverviewGrid from "@/components/overview/OverviewGrid.vue";

  @Component({
    components: {OverviewGrid}
  })
  export default class Overview extends Vue {

    private dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(
        "dd.MM.yyyy"
    );

    get year(): number {
      return OverviewNavigationStore.year;
    }

    set year(value: number) {
      OverviewNavigationStore.setYear(value);
      this.fetchTransactions();
    }

    get month(): string {
      return OverviewNavigationStore.month.name();
    }

    set month(month: string) {
      OverviewNavigationStore.setMonth(month);
      this.fetchTransactions();
    }

    get allMonths(): string[] {
      return TimeService.getAllMonths().map(value => value.name());
    }

    fetchTransactions() {
      let transactionGetRequest: TransactionGetRequestImpl = new TransactionGetRequestImpl(this.firstDateString, this.lastDateString);
      TransactionStore.loadTransactions(transactionGetRequest);
    }

    get dates(): LocalDate[] {
      return TimeService.getDatesFor(this.year, Month.valueOf(this.month));
    }

    get firstDateString(): string {
      return this.dates[0].format(this.dateFormatter);
    }

    get lastDateString(): string {
      return this.dates[this.dates.length - 1].format(this.dateFormatter);
    }

    created() {
      this.fetchTransactions();
    }

  }
</script>

<style scoped>

</style>