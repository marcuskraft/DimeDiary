<template>
  <div>
    <v-skeleton-loader
        class="mx-auto"
        max-width="80%"
        type="card" v-if="isLoading"
    ></v-skeleton-loader>

    <v-card v-else class="transaction-overview" max-width="60%" min-width="400px" outlined
            elevation="2" rounded>
      <v-container>
        <v-row justify="center">
          <v-col v-if="actualBalanceString !== ''">
            <v-chip max-width="300px" rounded
                    :color="actualBalanceEuroCent >= 0 ? 'success' : 'error'" elevation="11">
              <h2>Aktueller Kontostand: {{ actualBalanceString }}</h2>
            </v-chip>
          </v-col>
          <v-col>
            <v-btn color="primary" @click="addContinuousTransaction">Neue Transaktionsreihe</v-btn>
          </v-col>
        </v-row>


        <v-row justify="center">
          <v-col cols="auto">
            <v-text-field v-model="searchStringMember" outlined filled @focusout="searchChanged"
                          label="Transaktion suchen"></v-text-field>
          </v-col>
          <v-col cols="auto">
            <v-select
                outlined filled
                :items="bankAccounts"
                item-text="name"
                v-model="selectedBankAccount"
                :menu-props="{ maxHeight: '400'}"
                label="Bankkonten"
                return-object
                hint="Wähle die Bankkonten, für die Transaktionen angezeigt werden sollen"
                @change="reload"
            ></v-select>
          </v-col>
          <v-col cols="auto">
            <v-select
                outlined filled
                :items="categories"
                item-text="name"
                v-model="selectedCategories"
                label="Kategorien"
                return-object
                multiple
                clearable>
              <template v-slot:selection="{ item, index }">
                <span v-if="index === 0">{{ item.name }}</span>
                <span
                    v-if="index === 1"
                    class="grey--text caption">(+{{ selectedCategories.length - 1 }} andere)</span>
              </template>
            </v-select>
          </v-col>
        </v-row>
        <div v-for="continuousTransaction in continuousTransactions"
             :id="ref(continuousTransaction)"
             :transaction-prop="continuousTransaction"
             :key="continuousTransaction.id">{{ continuousTransaction.name }}
        </div>
      </v-container>
    </v-card>
  </div>
</template>

<script lang="ts">
import {Component, Vue} from "vue-property-decorator";
import {DateTimeFormatter, LocalDate} from "@js-joda/core";
import DatePickerTextFieldRange from "@/components/common/DatePickerTextFieldRange.vue";
import BankAccountStore from '@/store/modules/BankAccountStore';
import ContinuousTransactionStore from "@/store/modules/ContinuousTransactionStore";
import ContinuousTransactionModel from "@/model/ContinuousTransactionModel";
import BankAccountModel from "@/model/BankAccountModel";
import {LoadContinuousTransactionRequestImpl} from "@/rest-services/ContinuousRestService";
import TimeService from "@/helper/TimeService";
import AmountHelper from "@/helper/AmountHelper";
import BalanceStore from "@/store/modules/BalanceStore";
import CategoryModel from "@/model/CategoryModel";
import CategoryStore from "@/store/modules/CategoryStore";

@Component({
  components: {
    DatePickerTextFieldRange
  }
})
export default class ContinuousTransactionOverview extends Vue {

  private isLoading: boolean = true;

  private dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

  private readonly CATEGORY = "category";

  private searchStringMember: string = "";

  created() {
    BankAccountStore.loadBankAccountsIfNotPresent().then(value => {
      let bankAccountId = this.$route.query.bankAccountId;
      if (bankAccountId === "" || this.bankAccounts.find(value => value.id === bankAccountId) ===
          undefined) {
        this.selectedBankAccount = this.bankAccounts[0];
      }
      this.reload();
    });
    if (this.searchString !== "") {
      this.searchStringMember = this.searchString;
    }
  }


  get bankAccounts(): BankAccountModel[] {
    return BankAccountStore.bankAccounts;
  }


  get continuousTransactions(): ContinuousTransactionModel[] {
    return ContinuousTransactionStore.continuousTransactions.filter(
        continuousTransaction => continuousTransaction.bankAccount!.id ===
            this.selectedBankAccount!.id).
    filter(continuousTransaction => {
      if (this.selectedCategories.length > 0) {
        return this.selectedCategories.find(
            category => continuousTransaction.category!.id === category.id) !== undefined;
      }
      else {
        return true;
      }
    }).filter(continuousTransaction => {
      let search = this.searchString.trim().toLowerCase();
      if (search !== "") {
        return continuousTransaction.name.trim().toLowerCase().includes(search);
      }
      else {
        return true;
      }
    }).sort((a, b) => a.amountEuroCent > b.amountEuroCent ? 1 : -1);
  }

  get categories(): CategoryModel[] {
    return CategoryStore.categories;
  }


  get selectedCategories(): CategoryModel[] {
    let categoriesRet: CategoryModel[] = [];
    let categoryString = this.$route.query.category;
    if (categoryString !== undefined && categoryString !== "") {
      if (typeof categoryString === "string") {
        let category = this.categories.find(value => value.id === categoryString);
        if (category !== undefined) {
          categoriesRet.push(category);
        }
      }
      else {
        categoryString.forEach(value => {
          let category = this.categories.find(value1 => value1.id === value);
          if (category !== undefined) {
            categoriesRet.push(category);
          }
        })
      }
    }

    return categoriesRet;
  }

  set selectedCategories(value: CategoryModel[]) {
    this.buildRoute(this.selectedBankAccount, value, this.searchString);
  }

  get searchString(): string {
    let searchParameter = this.$route.query.search;
    if (searchParameter !== undefined && searchParameter !== "") {
      return searchParameter as string;
    }
    else {
      return ""
    }
  }

  set searchString(value: string) {
    this.buildRoute(this.selectedBankAccount, this.selectedCategories, value);
  }

  searchChanged() {
    this.searchString = this.searchStringMember;
  }


  buildRoute(bankAccount: BankAccountModel | undefined, categories: CategoryModel[],
      searchString: string) {
    let location = "continuous-transactions";
    let isParameterAlreadyThere: boolean = false;

    if (bankAccount !== undefined) {
      location = location + "?bankAccountId=" + bankAccount.id;
      isParameterAlreadyThere = true;
    }

    if (searchString !== "") {
      if (!isParameterAlreadyThere) {
        location = location + "?" + "search" + "=" + searchString;
        isParameterAlreadyThere = true;
      }
      else {
        location = location + "&" + "search" + "=" + searchString;
      }
    }

    categories.forEach(value => {
      if (!isParameterAlreadyThere) {
        location = location + "?" + this.CATEGORY + "=" + value.id;
        isParameterAlreadyThere = true;
      }
      else {
        location = location + "&category=" + value.id;
      }
    })
    this.$router.push(location)
  }


  get actualBalanceEuroCent(): number {
    if (this.selectedBankAccount !== undefined) {
      let balance = BalanceStore.balances.find(
          value => value.bankAccountId === this.selectedBankAccount!.id &&
              value.date.isEqual(LocalDate.now()));
      if (balance !== undefined) {
        return balance.balanceEuroCent;
      }
    }
    return 0;
  }

  get actualBalanceString(): string {
    return AmountHelper.euroCentToStringWithEuroSign(this.actualBalanceEuroCent);
  }

  ref(continuousTransactionModel: ContinuousTransactionModel): string {
    return "ref" + continuousTransactionModel.id!;
  }


  set selectedBankAccount(bankAccount: BankAccountModel | undefined) {
    if (bankAccount !== undefined) {
      this.buildRoute(bankAccount, this.selectedCategories, this.searchString);
      //this.reload();
    }
  }

  get selectedBankAccount(): BankAccountModel | undefined {
    let bankAccountId = this.$route.query.bankAccountId;
    if (bankAccountId !== "") {
      let bankAccountModel = this.bankAccounts.find(
          value => value.id === bankAccountId);
      if (bankAccountModel !== undefined) {
        return bankAccountModel;
      }
    }
    return undefined;
  }


  addContinuousTransaction() {
    this.$router.push("/continuous-transaction")
  }


  private reload() {
    this.loadContinuousTransactions();
  }

  private loadContinuousTransactions() {
    if (this.selectedBankAccount !== undefined) {
      let dateFrom: LocalDate = LocalDate.now().withDayOfMonth(1);
      let dateUntil: LocalDate = dateFrom.withDayOfMonth(dateFrom.lengthOfMonth());
      let loadContinuousTransactionRequestImpl: LoadContinuousTransactionRequestImpl = new LoadContinuousTransactionRequestImpl(
          this.selectedBankAccount.id!, TimeService.localDateToIsoString(dateFrom),
          TimeService.localDateToIsoString(dateUntil));
      ContinuousTransactionStore.loadContinuousTransactions(loadContinuousTransactionRequestImpl).
      then(value => {
        this.isLoading = false;
      });
    }
    else {
      this.isLoading = false;
    }
  }

}
</script>

<style scoped>

.transaction-overview {
  align-items: center;
  margin: auto;
}

</style>