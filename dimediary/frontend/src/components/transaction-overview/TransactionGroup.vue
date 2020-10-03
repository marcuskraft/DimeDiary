<template>
  <div class="transaction-group">
    <v-row>
      <v-col cols="1">
        <date-picker-text-field :set-local-date="setLocalDate"
                                :local-date="dateTemp"></date-picker-text-field>
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
import DatePickerTextField from "@/components/common/DatePickerTextField.vue";
import AmountHelper from "@/helper/AmountHelper";

@Component({
  components: {DatePickerTextField}
})
export default class TransactionGroup extends Vue {

  @Prop({type: TransactionModel}) transactionProp!: TransactionModel;

  private readonly dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private readonly dateTimeFormatterUser = DateTimeFormatter.ofPattern("dd.MM.yyyy");

  private dateTemp: LocalDate;

  constructor() {
    super();
    this.dateTemp = this.transactionProp.date;
  }

  setLocalDate(localDate: LocalDate) {
    this.dateTemp = localDate;
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
    return AmountHelper.onlyTwoPrecision(value);
  }


  save() {
    if (this.onlyTwoPrecision(this.amount)) {
      let transactionService: TransactionService = new TransactionService();
      this.transactionProp.date = this.dateTemp;
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