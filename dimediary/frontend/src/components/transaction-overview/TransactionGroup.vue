<template>
  <div style="border: #0d47a1; border-style: solid; margin: 2px">
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
                v-model="date"
                readonly
                v-on="on"
            ></v-text-field>
          </template>
          <v-date-picker
              v-model="date"
              no-title
              scrollable>
            <v-spacer></v-spacer>
            <v-btn
                text
                color="primary"
                @click="menu = false">
              Cancel
            </v-btn>
            <v-btn
                text
                color="primary"
                @click="$refs.menu.save(date)">
              OK
            </v-btn>
          </v-date-picker>
        </v-menu>
      </v-col>
      <v-col cols="1">
        <v-text-field
            :value="transactionProp.name"
            solo
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
  private menu: boolean = false;

  get date(): string {
    return this.transactionProp.date.format(this.dateTimeFormatter)
  }

  set date(date: string) {
    this.transactionProp.date = LocalDate.parse(date, this.dateTimeFormatter);
  }

}
</script>

<style scoped>

</style>