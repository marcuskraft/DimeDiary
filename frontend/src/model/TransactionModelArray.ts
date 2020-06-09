import TransactionModel from './TransactionModel';

export default class TransactionModelArray {

  private _transactions: Array<TransactionModel>;

  constructor(transactions: Array<TransactionModel>) {
    this._transactions = transactions;
  }


  /**
   * Getter $transactions
   * @return {Array<TransactionModel>}
   */
  public get transactions(): Array<TransactionModel> {
    return this._transactions;
  }

  /**
   * Setter $transactions
   * @param {Array<TransactionModel>} value
   */
  public set transactions(value: Array<TransactionModel>) {
    this._transactions = value;
  }

  public addTransaction(transaction: TransactionModel) {
    if (!this._transactions.includes(transaction)) {
      this._transactions.push(transaction);
    }
  }

}
