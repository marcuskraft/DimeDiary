<template>
  <div>
    <v-row>
      <v-col cols="2">
        <v-select
            :items="bankAccounts"
            :menu-props="{ maxHeight: '400'}"
            label="Bankkonten"
            multiple
            hint="Wähle die Bankkonten, für die Transaktionen angezeigt werden sollen"
        ></v-select>
      </v-col>
      <v-col cols="2">
        <v-menu
            ref="menu"
            v-model="datePicker"
            :close-on-content-click="false"
            :return-value.sync="date"
            transition="scale-transition"
            offset-y>
          <template v-slot:activator="{ on, attrs }">
            <v-text-field
                v-model="dateString"
                label="Datumsbereich"
                prepend-icon="mdi-calendar"
                readonly
                v-bind="attrs"
                v-on="on"
            ></v-text-field>
          </template>
          <v-date-picker
              v-model="date"
              no-title
              scrollable
              range>
            <v-spacer></v-spacer>
            <v-btn
                text
                color="primary"
                @click="datePicker = false">
              Abbrechen
            </v-btn>
            <v-btn
                text
                color="primary"
                @click="$refs.menu.save(date); loadTransactions();">
              OK
            </v-btn>
          </v-date-picker>
        </v-menu>
      </v-col>
    </v-row>
    <transaction-group v-for="transaction in transactions"
                       :transaction-prop="transaction" :key="transaction.id"></transaction-group>
  </div>
</template>

<script lang="ts">
import {Component, Vue} from "vue-property-decorator";
import {DateTimeFormatter, LocalDate} from "@js-joda/core";
import TransactionModel from "@/model/TransactionModel";
import TransactionStore from "@/store/modules/TransactionStore";
import {TransactionGetRequestImpl} from "@/rest-services/TransactionRestService";
import TimeService from "@/helper/TimeService";
import TransactionGroup from "@/components/transaction-overview/TransactionGroup.vue";

@Component({
  components: {
    TransactionGroup
  }
})
export default class TransactionOverview extends Vue {

  actualLocalDate: LocalDate = LocalDate.now();
  firstLocalDate: LocalDate | undefined = LocalDate.of(this.actualLocalDate.year(),
      this.actualLocalDate.month(), 1);
  lastLocalDate: LocalDate | undefined = LocalDate.of(this.actualLocalDate.year(),
      this.actualLocalDate.month(), this.actualLocalDate.lengthOfMonth())

  datePicker: boolean = false;

  private readonly dateFormatterTechnical = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private readonly dateFormatterUser = DateTimeFormatter.ofPattern("dd.MM.yyyy");

  get date(): string[] {
    let dateStrings: string[] = [];
    if (this.firstLocalDate !== undefined) {
      dateStrings.push(this.dateFormatterTechnical.format(this.firstLocalDate));
    }
    if (this.lastLocalDate !== undefined) {
      dateStrings.push(this.dateFormatterTechnical.format(this.lastLocalDate));
    }
    return dateStrings;
  }

  set date(dates: string[]) {
    if (dates.length == 0) {
      this.firstLocalDate = undefined;
      this.lastLocalDate = undefined;
    }
    else if (dates.length == 1) {
      this.firstLocalDate = LocalDate.parse(dates[0], this.dateFormatterTechnical);
      this.lastLocalDate = undefined;
    }
    else {
      let firstLocalDateTemp = LocalDate.parse(dates[0], this.dateFormatterTechnical);
      let lastLocalDateTemp = LocalDate.parse(dates[1], this.dateFormatterTechnical);
      this.firstLocalDate =
          firstLocalDateTemp.isBefore(lastLocalDateTemp) ? firstLocalDateTemp : lastLocalDateTemp;
      this.lastLocalDate =
          lastLocalDateTemp.isAfter(firstLocalDateTemp) ? lastLocalDateTemp : firstLocalDateTemp;
    }
  }

  get dateString(): string {
    let value: string = "";

    if (this.firstLocalDate !== undefined) {
      value = this.dateFormatterUser.format(this.firstLocalDate);
      if (this.lastLocalDate !== undefined) {
        value += " - " + this.dateFormatterUser.format(this.lastLocalDate);
      }
    }
    return value;
  }

  get bankAccounts(): string[] {
    return ["Sparkasse", "N26"];
  }

  get transactions(): TransactionModel[] {
    return TransactionStore.transactions;
  }

  private loadTransactions() {
    this.datePicker = false;
    if (this.firstLocalDate !== undefined && this.lastLocalDate !== undefined) {
      let request: TransactionGetRequestImpl = new TransactionGetRequestImpl(
          "941d4a63-72a7-4fcd-a669-742323b486c5",
          TimeService.localDateToDate(this.firstLocalDate)!,
          TimeService.localDateToDate(this.lastLocalDate)!);
      TransactionStore.loadTransactions(request);
    }
  }

}
</script>

<style scoped>

</style>