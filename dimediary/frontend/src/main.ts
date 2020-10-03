import Vue from 'vue'
import App from './App.vue'
import store from './store'

import configureEnvironment from './config/Environment';
import vuetify from './plugins/vuetify';
import router from './router'

// Set environment variables on window object
configureEnvironment();

Vue.config.productionTip = false

new Vue({
  vuetify,
  store,
  router,
  render: h => h(App)
}).$mount('#app')
