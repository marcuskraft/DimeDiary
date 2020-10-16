<template>
  <v-menu
      ref="menu"
      v-model="menu"
      :close-on-content-click="false"
      :return-value.sync="dates"
      transition="scale-transition"
      offset-y
      min-width="290px">
    <template v-slot:activator="{ on, attrs }">
      <v-btn
          outlined
          readonly
          v-on="on"
          x-large
      >
        {{ label }}
      </v-btn>
    </template>
    <v-date-picker
        v-model="dates"
        no-title
        scrollable
        locale="GERMANY"
        range>
      <v-btn icon @click=" $refs.menu.save(); clear();">
        <v-icon>clear</v-icon>
      </v-btn>
      <v-spacer></v-spacer>
      <v-btn
          text
          color="primary"
          @click="menu = false"
      >
        Abbrechen
      </v-btn>
      <v-btn
          text
          color="primary"
          @click="$refs.menu.save(); save();"
      >
        OK
      </v-btn>
    </v-date-picker>
  </v-menu>
</template>

<script lang="ts">
import {Component, Prop, Vue} from "vue-property-decorator";
import {DateTimeFormatter, LocalDate} from "@js-joda/core";

@Component
export default class DatePickerTextFieldRange extends Vue {

  @Prop({type: Function, required: true}) setLocalDates!: Function;
  @Prop({type: Array}) localDates?: LocalDate[] | undefined;
  @Prop({type: String, default: "Datum", required: false}) label?: string;

  private readonly dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private readonly dateTimeFormatterUser = DateTimeFormatter.ofPattern("dd.MM.yyyy");
  private menu: boolean = false;

  private datesMember: string[] = this.localDates !== undefined ? this.localDates.map(
      date => date.format(this.dateTimeFormatter)) : [];

  get dates(): string[] {
    return this.datesMember;
  }

  set dates(value: string[]) {
    this.datesMember = value;
  }


  get sortedLocalDates(): LocalDate[] {
    return this.dates.map(date => LocalDate.parse(date, this.dateTimeFormatter)).
    sort((a, b) => a.compareTo(b));
  }

  get dateString(): string {
    if (this.sortedLocalDates.length == 0) {
      return "";
    }
    else if (this.sortedLocalDates.length == 1) {
      return this.sortedLocalDates[0].format(this.dateTimeFormatterUser);
    }
    else {
      return this.sortedLocalDates[0].format(this.dateTimeFormatterUser) + " ... " +
          this.sortedLocalDates[1].format(this.dateTimeFormatterUser);
    }
  }

  calculateAllLocalDatesBetween(): LocalDate[] {
    let dates: LocalDate[] = this.sortedLocalDates;
    if (dates.length > 1) {
      let date: LocalDate = dates[0].plusDays(1);
      let lastDate: LocalDate = dates[dates.length - 1];
      while (date.isBefore(lastDate)) {
        dates.push(date);
        date = date.plusDays(1);
      }
    }
    return dates;
  }

  clear() {
    this.dates = [];
    this.setLocalDates(undefined);
  }

  save() {
    this.setLocalDates(this.calculateAllLocalDatesBetween());
  }

}
</script>

<style scoped>


</style>