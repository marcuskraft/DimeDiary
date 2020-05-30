<template>
  <v-slide-group multiple show-arrows>
    <v-slide-item
      v-for="(transaction,n) in transactions"
      :key="n"
      v-slot:default="{ active, toggle }"
    >
      <v-btn
        :input-value="active"
        active-class="purple white--text"
        depressed
        rounded
        @click="toggle"
      >{{ transaction.subject }}</v-btn>
    </v-slide-item>
  </v-slide-group>
</template>

<script lang="ts">
import { Component, Vue, Prop } from "vue-property-decorator";
import TransactionModel from "../model/TransactionModel";
import TransactionModelArray from "../model/TransactionModelArray";

@Component
export default class TransactionSlideGroup extends Vue {
  @Prop({ type: TransactionModelArray })
  transactionsProp!: TransactionModelArray;

  public get transactions(): TransactionModel[] {
    return this.transactionsProp.transactions;
  }
}
</script>

<style scoped>
</style>
