<template>
  <div id="app">
    <!-- App.vue -->

    <v-app>
      <v-app-bar app>
        Missing content
      </v-app-bar>

      <v-navigation-drawer app>
        <overview-navigation></overview-navigation>
      </v-navigation-drawer>

      <v-content app>
        <overview-grid></overview-grid>
      </v-content>
    </v-app>
  </div>
</template>

<script lang="ts">
  import {Component, Vue} from "vue-property-decorator";
  import OverviewGrid from "@/components/overview/OverviewGrid.vue";
  import OverviewNavigation from "@/components/overview/OverviewNavigation.vue";

  @Component({
    components: {
      OverviewGrid,
      OverviewNavigation
    }
  })
  export default class App extends Vue {
  }
</script>

<style>
</style>
