import Vue from 'vue'
import VueRouter, {RouteConfig} from 'vue-router'
import Dashboard from "@/components/Dashboard.vue";
import TransactionOverview from "@/components/transaction-overview/TransactionOverview.vue";

Vue.use(VueRouter)

const routes: Array<RouteConfig> = [
  {
    path: '/',
    name: 'Dashboard',
    component: Dashboard
  },
  {
    path: '/transactions',
    name: 'Transactions',
    component: TransactionOverview
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
