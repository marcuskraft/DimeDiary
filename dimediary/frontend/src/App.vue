<template>
  <div id="app">
    <!-- App.vue -->

    <v-app>
      <v-app-bar app>
        Missing content
      </v-app-bar>

      <v-navigation-drawer app>
        <overview-navigation></overview-navigation>
      </v-navigation-drawer>

      <v-content app>
        <overview-grid v-if="showOverviewGrid"
                       :day-transactions-array="dayTransactionArray"></overview-grid>
      </v-content>
    </v-app>
  </div>
</template>

<script lang="ts">
  import {Component, Vue} from "vue-property-decorator";
  import OverviewGrid from "@/components/overview/OverviewGrid.vue";
  import OverviewNavigation from "@/components/overview/OverviewNavigation.vue";
  import OverviewNavigationStore from "@/store/modules/OverviewNavigationStore";
  import {DayTransactionsArray} from "@/model/DayTransactions";
  import TransactionStore from "@/store/modules/TransactionStore";
  import {Month} from "@js-joda/core";

  @Component({
    components: {
      OverviewGrid,
      OverviewNavigation
    }
  })
  export default class App extends Vue {

    get year(): number {
      return OverviewNavigationStore.year;
    }

    get month(): Month {
      return OverviewNavigationStore.month;
    }

    get showOverviewGrid(): boolean {
      return this.dayTransactionArray.dayTransactions.length != 0;
    }

    get dayTransactionArray(): DayTransactionsArray {
      return TransactionStore.transactions;
    }


  }
</script>

<style>
</style>
