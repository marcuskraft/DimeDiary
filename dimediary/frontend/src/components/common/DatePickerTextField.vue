<template>
  <v-menu
      ref="menu"
      v-model="menu"
      :close-on-content-click="false"
      :return-value.sync="dateTemp"
      transition="scale-transition"
      offset-y
      min-width="290px">
    <template v-slot:activator="{ on, attrs }">
      <v-text-field v-if="solo"
                    v-model="dateString"
                    :label="label"
                    readonly
                    v-on="on"
                    solo
      ></v-text-field>
      <v-text-field v-else
                    v-model="dateString"
                    :label="label"
                    readonly
                    v-on="on"
      ></v-text-field>
    </template>
    <v-date-picker
        v-model="dateTemp"
        no-title
        scrollable
        locale="GERMANY">
      <v-spacer></v-spacer>
      <v-btn
          text
          color="primary"
          @click="menu = false">
        Abbrechen
      </v-btn>
      <v-btn
          text
          color="primary"
          @click="$refs.menu.save(dateTemp); save();">
        OK
      </v-btn>
    </v-date-picker>
  </v-menu>
</template>

<script lang="ts">
import {Component, Prop, Vue} from "vue-property-decorator";
import {DateTimeFormatter, LocalDate} from "@js-joda/core";

@Component
export default class DatePickerTextField extends Vue {

  @Prop({type: Function, required: true}) setLocalDate?: Function;
  @Prop({type: Object}) localDate?: LocalDate;
  @Prop({type: String, default: "Datum", required: false}) label?: string;
  @Prop({type: Boolean, default: false, required: false}) solo?: boolean;

  private readonly dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private readonly dateTimeFormatterUser = DateTimeFormatter.ofPattern("dd.MM.yyyy");
  private menu: boolean = false;

  private dateTemp: string = this.localDate !== undefined ? this.localDate.format(
      this.dateTimeFormatter) : LocalDate.now().format(this.dateTimeFormatter)


  get dateString(): string {
    return LocalDate.parse(this.dateTemp, this.dateTimeFormatter).format(this.dateTimeFormatterUser)
  }

  save() {
    if (this.setLocalDate !== undefined) {
      this.setLocalDate(LocalDate.parse(this.dateTemp, this.dateTimeFormatter));
    }
  }

}
</script>

<style scoped>


</style>