<template>
  <div id="app">
    <v-app>
      <v-navigation-drawer app>
        <left-navigation></left-navigation>
      </v-navigation-drawer>
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
import BankAccountStore from "@/store/modules/BankAccountStore";

@Component({
  components: {
    TransactionOverview,
    LeftNavigation
  }
})
export default class App extends Vue {

  mounted() {
    CategoryStore.loadCategories();
    BankAccountStore.loadBankAccounts();
  }

}
</script>

<style>

</style>
