<template>
  <div class="transaction-group">
    <v-row>
      <v-col cols="1">
        <v-menu
            ref="menu"
            v-model="menu"
            :close-on-content-click="false"
            :return-value.sync="date"
            transition="scale-transition"
            offset-y
            min-width="290px">
          <template v-slot:activator="{ on, attrs }">
            <v-text-field
                v-model="dateString"
                readonly
                v-on="on"
                solo
            ></v-text-field>
          </template>
          <v-date-picker
              v-model="date"
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
                @click="$refs.menu.save(date); save();">
              OK
            </v-btn>
          </v-date-picker>
        </v-menu>
      </v-col>
      <v-col cols="1">
        <v-text-field
            v-model="name"
            type="text"
            solo
            @change="save"
        ></v-text-field>
      </v-col>
    </v-row>
  </div>
</template>

<script lang="ts">
import {Component, Prop, Vue} from "vue-property-decorator";
import TransactionModel from "@/model/TransactionModel";
import {DateTimeFormatter, LocalDate} from "@js-joda/core";

@Component
export default class TransactionGroup extends Vue {

  @Prop({type: TransactionModel}) transactionProp!: TransactionModel;

  private readonly dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private readonly dateTimeFormatterUser = DateTimeFormatter.ofPattern("dd.MM.yyyy");
  private menu: boolean = false;

  get date(): string {
    return this.transactionProp.date.format(this.dateTimeFormatter)
  }

  set date(date: string) {
    this.transactionProp.date = LocalDate.parse(date, this.dateTimeFormatter);
  }

  get dateString(): string {
    return this.transactionProp.date.format(this.dateTimeFormatterUser)
  }

  get name(): string {
    return this.transactionProp.name;
  }

  set name(value: string) {
    this.transactionProp.name = value;
  }

  save() {
    console.info("saved");
  }

}
</script>

<style scoped>

.transaction-group {
  background-color: #00b0ff;
  margin: 5px;
}

</style>