import { Module, VuexModule, Mutation, Action, getModule } from 'vuex-module-decorators'
import store from '@/store'
import TransactionModel from '@/model/TransactionModel';
import { TransactionTransformer } from '@/services/transformer/TransactionTransformer';
import { TransactionService } from '@/services/TransactionService';
import { Transactions } from 'build/openapi/models';


@Module({
    namespaced: true,
    name: 'TransactionStore',
    store,
    dynamic: true
})
export class TransactionStore extends VuexModule {

    private _transactions: Array<TransactionModel> = [];

    @Mutation
    addTransactions(transactions: Transactions) {

        transactions.transactions?.forEach(transaction => {
            this._transactions.push(TransactionTransformer.from(transaction));
        })
    }

    @Mutation
    clearTransactions() {
        this._transactions.splice(0, this._transactions.length);
    }


    @Action
    loadTransactions() {
        let transactionsService: TransactionService = new TransactionService();
        transactionsService.getTransactions().then((transactions) => {
            this.clearTransactions();
            this.addTransactions(transactions);
        })
    }

}

export default getModule(TransactionStore);