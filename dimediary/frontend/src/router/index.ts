import Vue from 'vue'
import VueRouter, {RouteConfig} from 'vue-router'
import Dashboard from "@/components/Dashboard.vue";
import TransactionOverview from "@/components/transaction-overview/TransactionOverview.vue";
import BankAccountOverview from "@/components/bank-account-overview/BankAccountOverview.vue";


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
    component: TransactionOverview
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
