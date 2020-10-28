<template>
  <v-container>
    <v-row justify="center" v-for="recurrenceDate in recurrenceDates"
           :key="recurrenceDate.localDate.toString()">
      <v-checkbox v-model="recurrenceDate.isActive"/>
      {{ getDateString(recurrenceDate.localDate) }}
    </v-row>
  </v-container>
</template>

<script lang="ts">
import {Component, Vue} from "vue-property-decorator";
import ContinuousTransactionModel from "@/model/ContinuousTransactionModel";
import ContinuousTransactionStore from "@/store/modules/ContinuousTransactionStore";
import {DateTimeFormatter, LocalDate} from "@js-joda/core";
import RecurrenceDate from "@/components/continuous-transaction-overview/RecurrenceDate";

@Component
export default class RecurrenceDatesOverview extends Vue {

  private dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

  get recurrenceDatesFromRecurrenceSettings(): LocalDate[] {
    return ContinuousTransactionStore.recurrenceDatesFromRecurrenceSettings;
  }


  getDateString(localDate: LocalDate): string {
    return this.dateTimeFormatter.format(localDate);
  }

  get recurrenceDates(): RecurrenceDate[] {
    let recurrenceDates: RecurrenceDate[] = [];
    this.recurrenceDatesFromRecurrenceSettings.forEach(
        localDate => recurrenceDates.push(
            new RecurrenceDate(localDate, !this.isException(localDate))));
    this.extraInstances.forEach(value => recurrenceDates.push(new RecurrenceDate(value)));
    return recurrenceDates.sort((a, b) => a.localDate.compareTo(b.localDate));
  }

  isException(localDate: LocalDate): boolean {
    return this.exceptions.includes(localDate);
  }

  get extraInstances(): LocalDate[] {
    return this.continuousTransaction.recurrenceExtraInstances;
  }

  get exceptions(): LocalDate[] {
    return this.continuousTransaction.recurrenceExceptions;
  }

  get continuousTransaction(): ContinuousTransactionModel {
    return ContinuousTransactionStore.selectedContinuousTransaction!;
  }

}
</script>

<style scoped>

</style>