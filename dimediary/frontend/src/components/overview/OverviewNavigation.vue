<template>
  <div>
    <v-list dense nav>
      <v-list-item>
        <v-text-field
            label="Jahr"
            v-model.number="year"
            type="number"
        ></v-text-field>
      </v-list-item>
      <v-list-item>
        <v-select
            label="Monat"
            :items="allMonths"
            v-model="month"
        >
        </v-select>
      </v-list-item>
      <v-list-item>
        <v-btn @click="fetchTransactions">
          Laden
        </v-btn>
      </v-list-item>
    </v-list>


  </div>
</template>

<script lang="ts">
  import {Component, Vue} from "vue-property-decorator";
  import OverviewNavigationStore from "@/store/modules/OverviewNavigationStore";
  import TimeService from "@/helper/TimeService";
  import {TransactionGetRequestImpl} from "@/services/TransactionService";
  import TransactionStore from "@/store/modules/TransactionStore";
  import {DateTimeFormatter, LocalDate, Month} from "@js-joda/core";

  @Component
  export default class OverviewNavigation extends Vue {

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


  }
</script>

<style scoped>

</style>