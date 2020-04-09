<template>
  <div id="app">
    <!-- App.vue -->

    <v-app>
      <v-navigation-drawer app>
        Navigation Drawer
      </v-navigation-drawer>

      <v-app-bar app>
        <!-- -->
      </v-app-bar>

      <!-- Sizes your content based upon application components -->
      <v-content>
        <div>Test</div>
      </v-content>

      <v-footer app>
        <!-- -->
      </v-footer>
    </v-app>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import LikeButton from "./components/LikeButton.vue";
import Vuetify from "vuetify";

Vue.use(Vuetify);

@Component({
  components: {
    LikeButton: LikeButton
  }
})
export default class App extends Vue {}
</script>

<style>
</style>
