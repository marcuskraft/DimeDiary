<template>
  <v-app-bar color="orange" app>
    <v-toolbar-title>DimeDiary</v-toolbar-title>

    <v-spacer></v-spacer>

    <v-btn icon elevation="3" @click="$router.push('dashboard')">
      <v-icon>mdi-view-dashboard</v-icon>
    </v-btn>

    <v-btn icon elevation="3" @click="$router.push('transactions')">
      <v-icon>polymer</v-icon>
    </v-btn>

    <v-btn icon elevation="3" @click="$router.push('bankaccounts')">
      <v-icon>account_balance</v-icon>
    </v-btn>

    <v-spacer></v-spacer>

  </v-app-bar>
</template>

<script lang="ts">


import {Component, Vue} from "vue-property-decorator";


@Component
export default class AppBar extends Vue {


}

</script>

<style scoped>


</style>