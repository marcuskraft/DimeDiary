<template>
  <div>
    <v-list dense nav>
      <v-list-item>
        <v-text-field label="Jahr" type="number" v-model="year"></v-text-field>
      </v-list-item>
      <v-list-item>
        <v-select
            label="Monat"
            :items="allMonths"
            v-model="month"
        >
        </v-select>
      </v-list-item>
    </v-list>


  </div>
</template>

<script lang="ts">
  import {Component, Vue} from "vue-property-decorator";
  import OverviewNavigationStore from "@/store/modules/OverviewNavigationStore";
  import TimeService from "@/helper/TimeService";

  @Component
  export default class OverviewNavigation extends Vue {


    get year(): number {
      return OverviewNavigationStore.year;
    }

    set year(value: number) {
      OverviewNavigationStore.setYear(value)
    }

    get month(): string {
      return OverviewNavigationStore.month.name()
    }

    set month(month: string) {
      OverviewNavigationStore.setMonth(month)
    }

    get allMonths(): string[] {
      return TimeService.getAllMonths().map(value => value.name());
    }


  }
</script>

<style scoped>

</style>