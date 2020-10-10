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
import TransactionOverview from "@/components/transaction-overview/TransactionOverview.vue";
import CategoryStore from "@/store/modules/CategoryStore";
import BankAccountCategoryStore from "@/store/modules/BankAccountCategoryStore";
import AppBar from "@/components/AppBar.vue";
import BankAccountStore from "@/store/modules/BankAccountStore";

@Component({
  components: {
    AppBar,
    TransactionOverview
  }
})
export default class App extends Vue {

  created() {
    CategoryStore.loadCategories();
    BankAccountStore.loadBankAccounts();
    BankAccountCategoryStore.loadBankAccountCategories();
  }

}
</script>

<style>

</style>
