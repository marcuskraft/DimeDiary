import Vue from 'vue'
import App from './App.vue'

import configureEnvironment from './config/Environment';
import vuetify from './plugins/vuetify';

// Set environment variables on window object
configureEnvironment();

Vue.config.productionTip = false

new Vue({
  vuetify,
  render: h => h(App)
}).$mount('#app')
