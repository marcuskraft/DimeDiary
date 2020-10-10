<template>
  <div id="app">
    <v-app>
      <app-bar app></app-bar>
      <v-main>
        <router-view/>
      </v-main>
    </v-app>
  </div>
</template>

<script lang="ts">
import {Component, Vue} from "vue-property-decorator";
import LeftNavigation from "@/components/LeftNavigation.vue";
import TransactionOverview from "@/components/transaction-overview/TransactionOverview.vue";
import CategoryStore from "@/store/modules/CategoryStore";
import BankAccountCategoryStore from "@/store/modules/BankAccountCategoryStore";
import AppBar from "@/components/AppBar.vue";

@Component({
  components: {
    AppBar,
    TransactionOverview,
    LeftNavigation
  }
})
export default class App extends Vue {

  created() {
    CategoryStore.loadCategories();
    BankAccountCategoryStore.loadBankAccountCategories();
  }

}
</script>

<style>

</style>
