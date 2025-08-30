<template>
  <v-app-bar color="green" app data-ref="app-bar">
    <v-toolbar-title>DimeDiary</v-toolbar-title>

    <v-spacer></v-spacer>

    <v-btn icon elevation="3" @click="routeTo('/dashboard')" data-ref="dashboard">
      <v-icon>home</v-icon>
    </v-btn>

    <v-btn icon elevation="3" @click="routeTo('/transactions')" data-ref="transactions">
      <v-icon>euro_symbol</v-icon>
    </v-btn>

    <v-btn icon elevation="3" @click="routeTo('/continuous-transactions')" data-ref="continuous-transactions">
      <v-icon>repeat</v-icon>
    </v-btn>

    <v-btn icon elevation="3" @click="routeTo('/bankaccounts')" data-ref="bankaccounts">
      <v-icon>account_balance</v-icon>
    </v-btn>

    <v-spacer></v-spacer>

  </v-app-bar>
</template>

<script lang="ts">


import {Component, Vue} from "vue-property-decorator";


@Component
export default class AppBar extends Vue {

  routeTo(path: string) {
    if (this.$route.path !== path) {
      this.$router.push(path);
    }
  }


}

</script>

<style scoped>


</style>