import {TransactionGetRequestImpl} from "@/rest-services/TransactionRestService";
import TimeService from "@/helper/TimeService";
import {LocalDate} from "@js-joda/core";
import TransactionStore from "@/store/modules/TransactionStore";
import TransactionModel from "@/model/TransactionModel";
import BalanceStore from "@/store/modules/BalanceStore";

export default class TransactionService {

  public get transactions(): TransactionModel[] {
    return TransactionStore.transactions;
  }

  public loadTransactions(bankAccountId: string, dateFrom: LocalDate,
      dateUntil: LocalDate): Promise<void> {
    let request: TransactionGetRequestImpl = new TransactionGetRequestImpl(
        bankAccountId, TimeService.localDateToIsoString(dateFrom)!,
        TimeService.localDateToIsoString(dateUntil)!);
    return TransactionStore.loadTransactions(request);
  }

  public saveTransaction(transaction: TransactionModel) {
    TransactionStore.saveTransaction(transaction).then(transactionReceived => {
      if (transactionReceived.bankAccount !== undefined) {
        BalanceStore.reloadBalances(transactionReceived.bankAccount)
      }
    });
  }

  public deleteTransaction(transaction: TransactionModel) {
    TransactionStore.deleteTransaction(transaction).then(value => {
      if (transaction.bankAccount !== undefined) {
        BalanceStore.reloadBalances(transaction.bankAccount);
      }
    });
  }


}