import {BankAccountCategory} from '@/../build/openapi/models/BankAccountCategory';
import BankAccountModel from "@/model/BankAccountModel";
import {BankAccount} from '@/../build/openapi/models/BankAccount';
import {BankAccountCategoryTransformer} from "@/rest-services/transformer/BankAccountCategoryTransformer";
import TimeService from "@/helper/TimeService";

export class BankAccountTransformer {

  public static from(bankAccount?: BankAccountModel): BankAccount | undefined {
    if (bankAccount === undefined) {
      return undefined;
    }
    return new BankAccountRest(bankAccount.id, bankAccount.name, bankAccount.bankName,
        bankAccount.iban, bankAccount.bic,
        BankAccountCategoryTransformer.from(bankAccount.bankAccountCategory),
        TimeService.localDateToIsoString(bankAccount.dateStartBalance),
        bankAccount.startBalanceEuroCent);
  }

  public static to(bankAccount?: BankAccount): BankAccountModel | undefined {
    if (bankAccount === undefined) {
      return undefined;
    }
    return new BankAccountModel(bankAccount.name!,
        TimeService.isoStringToLocalDate(bankAccount.dateStartBalance!),
        bankAccount.startBalanceEuroCent!,
        BankAccountCategoryTransformer.to(bankAccount.bankAccountCategory!), bankAccount.id,
        bankAccount.bankName, bankAccount.iban, bankAccount.bic);
  }


}

class BankAccountRest {

  id?: string;
  name?: string;
  bankName?: string;
  iban?: string;
  bic?: string;
  bankAccountCategory?: BankAccountCategory;
  dateStartBalance?: string;
  startBalanceEuroCent?: number;


  constructor(id?: string, name?: string, bankName?: string, iban?: string, bic?: string,
      bankAccountCategory?: BankAccountCategory, dateStartBalance?: string,
      startBalanceEuroCent?: number) {
    this.id = id;
    this.name = name;
    this.bankName = bankName;
    this.iban = iban;
    this.bic = bic;
    this.bankAccountCategory = bankAccountCategory;
    this.dateStartBalance = dateStartBalance;
    this.startBalanceEuroCent = startBalanceEuroCent;
  }
}