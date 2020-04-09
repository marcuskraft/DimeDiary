import Vue from 'vue'
import App from './App.vue'

import configureEnvironment from './config/Environment';

// Set environment variables on window object
configureEnvironment();

Vue.config.productionTip = false

new Vue({
  render: h => h(App),
}).$mount('#app')
