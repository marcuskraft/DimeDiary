import Vue from 'vue'
import VueRouter, {RouteConfig} from 'vue-router'
import Dashboard from "@/components/Dashboard.vue";
import TransactionOverview from "@/components/transaction-overview/TransactionOverview.vue";
import BankAccountOverview from "@/components/bank-account-overview/BankAccountOverview.vue";
import Transaction from "@/components/transaction-overview/Transaction.vue";
import ContinuousTransactionOverview
  from "@/components/continuous-transaction-overview/ContinuousTransactionOverview.vue";
import ContinuousTransaction
  from "@/components/continuous-transaction-overview/ContinuousTransaction.vue";


Vue.use(VueRouter)

const routes: Array<RouteConfig> = [
  {path: '/', redirect: '/dashboard'},
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: Dashboard
  },
  {
    path: '/transactions',
    name: 'Transactions',
    component: TransactionOverview,
  },
  {
    path: '/continuous-transactions',
    name: 'Continuous-Transactions',
    component: ContinuousTransactionOverview,
  },
  {
    path: '/continuous-transaction',
    name: 'Continuous-Transaction',
    component: ContinuousTransaction,
  },
  {
    path: '/continuous-transaction/:continuousTransactionId',
    name: 'Continuous-Transaction-WithId',
    component: ContinuousTransaction,
    props: true
  },
  {
    path: '/transaction',
    name: 'Transaction',
    component: Transaction
  },
  {
    path: '/transaction/:transactionId',
    name: 'TransactionWithId',
    component: Transaction,
    props: true
  },
  {
    path: '/bankaccounts',
    name: 'BankAccounts',
    component: BankAccountOverview
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
