<template>
  <div id="app">
    <!-- App.vue -->

    <v-app>
      <app-bar></app-bar>

      <v-content app>
        <overview/>
      </v-content>
    </v-app>
  </div>
</template>

<script lang="ts">
import {Component, Vue} from "vue-property-decorator";
import OverviewGrid from "@/components/overview/OverviewGrid.vue";
import {DayTransactionsArray} from "@/model/DayTransactions";
import TransactionStore from "@/store/modules/TransactionStore";
import AppBar from "@/components/overview/Appbar.vue";
import Overview from "@/components/overview/Overview.vue";

@Component({
  components: {
    Overview,
    OverviewGrid,
    AppBar
  }
})
export default class App extends Vue {
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
