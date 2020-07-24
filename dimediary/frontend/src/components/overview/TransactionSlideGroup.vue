<template>
  <v-slide-group multiple show-arrows v-if="transactions.length > 0">
    <v-slide-item
        v-for="(transaction,n) in transactions"
        :key="n"
        v-slot:default="{ active, toggle }"
    >
      <v-chip>{{ transaction.subject }}: {{ transaction.amount }} €
      </v-chip>
    </v-slide-item>
  </v-slide-group>
</template>

<script lang="ts">
  import {Component, Prop, Vue} from "vue-property-decorator";
  import TransactionModel from "@/model/TransactionModel";
  import DayTransactions from "@/model/DayTransactions";

  @Component
  export default class TransactionSlideGroup extends Vue {
    @Prop({type: DayTransactions})
    transactionsProp!: DayTransactions;


    get transactions(): TransactionModel[] {
      return this.transactionsProp.transactions;
    }
  }
</script>

<style scoped>
</style>
