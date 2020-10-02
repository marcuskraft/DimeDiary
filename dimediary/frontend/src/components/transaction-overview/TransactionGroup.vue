<template>
  <div class="transaction-group">
    <v-row>
      <v-col cols="1">
        <v-menu
            ref="menu"
            v-model="menu"
            :close-on-content-click="false"
            :return-value.sync="dateTemp"
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
      </v-col>
      <v-col cols="1">
        <v-text-field
            v-model="name"
            type="text"
            solo
            @change="save"
        ></v-text-field>
      </v-col>
      <v-col cols="1">
        <v-form>
          <v-text-field
              v-model="amount"
              type="number"
              solo
              suffix="€"
              @change="save"
              :rules="[value => onlyTwoPrecision(value) || 'nur 2 Nachkommastellen möglich' ]"
          ></v-text-field>
        </v-form>
      </v-col>
    </v-row>
  </div>
</template>

<script lang="ts">
import {Component, Prop, Vue} from "vue-property-decorator";
import TransactionModel from "@/model/TransactionModel";
import {DateTimeFormatter, LocalDate} from "@js-joda/core";
import TransactionService from "@/service/TransactionService";

@Component
export default class TransactionGroup extends Vue {

  @Prop({type: TransactionModel}) transactionProp!: TransactionModel;

  private readonly dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private readonly dateTimeFormatterUser = DateTimeFormatter.ofPattern("dd.MM.yyyy");
  private menu: boolean = false;

  private dateTemp: string;

  constructor() {
    super();
    this.dateTemp = this.transactionProp.date.format(this.dateTimeFormatter);
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

  get amount(): number {
    return this.transactionProp.amountEuroCent / 100;
  }

  set amount(amount: number) {
    this.transactionProp.amountEuroCent = amount * 100;
  }

  onlyTwoPrecision(value: number): boolean {
    if (value != null && value.toLocaleString().indexOf(",") > -1 &&
        (value.toLocaleString().split(',')[1].length > 2)) {
      return false;
    }
    return true;
  }

  save() {
    if (this.onlyTwoPrecision(this.amount)) {
      let transactionService: TransactionService = new TransactionService();
      this.transactionProp.date = LocalDate.parse(this.dateTemp, this.dateTimeFormatter);
      transactionService.saveTransaction(this.transactionProp);
      console.info("saved");
    }
  }

}
</script>

<style scoped>

.transaction-group {
  background-color: #00b0ff;
  margin: 5px;
}

::v-deep input::-webkit-outer-spin-button,
::v-deep input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

</style>