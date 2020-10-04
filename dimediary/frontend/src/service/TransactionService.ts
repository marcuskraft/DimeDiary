import {TransactionGetRequestImpl} from "@/rest-services/TransactionRestService";
import TimeService from "@/helper/TimeService";
import {LocalDate} from "@js-joda/core";
import TransactionStore from "@/store/modules/TransactionStore";
import TransactionModel from "@/model/TransactionModel";

export default class TransactionService {

  public get transactions(): TransactionModel[] {
    return TransactionStore.transactions;
  }

  public loadTransactions(bankAccountId: string, dateFrom: LocalDate, dateUntil: LocalDate) {
    let request: TransactionGetRequestImpl = new TransactionGetRequestImpl(
        bankAccountId, TimeService.localDateToIsoString(dateFrom)!,
        TimeService.localDateToIsoString(dateUntil)!);
    TransactionStore.loadTransactions(request);
  }

  public saveTransaction(transaction: TransactionModel) {
    TransactionStore.saveTransaction(transaction);
  }

  public deleteTransaction(transaction: TransactionModel) {
    TransactionStore.deleteTransaction(transaction);
  }


}