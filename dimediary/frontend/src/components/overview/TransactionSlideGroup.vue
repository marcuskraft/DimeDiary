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
      >{{ transaction.subject }}: {{ transaction.amount }} €
      </v-btn>
    </v-slide-item>
  </v-slide-group>
</template>

<script lang="ts">
  import {Component, Prop, Vue} from "vue-property-decorator";
  import TransactionModel from "@/model/TransactionModel";
  import TransactionModelArray from "@/model/TransactionModelArray";

  @Component
  export default class TransactionSlideGroup extends Vue {
    @Prop({type: TransactionModelArray})
    transactionsProp!: TransactionModelArray;

    transactionArray: TransactionModelArray;

    constructor() {
      super();
      this.transactionArray = this.transactionsProp;
    }

    public get transactions(): TransactionModel[] {
      return this.transactionArray.transactions;
    }
  }
</script>

<style scoped>
</style>
