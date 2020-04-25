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
        <overview-grid></overview-grid>
      </v-content>

      <v-footer app>
        <!-- -->
      </v-footer>
    </v-app>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import OverviewGrid from "@/components/OverviewGrid.vue";


@Component({
  components: {
    OverviewGrid
  }
})
export default class App extends Vue {}
</script>

<style>
</style>
