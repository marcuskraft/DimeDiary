<template>
  <div id="app">
    <!-- App.vue -->

    <v-app>
      <!-- Sizes your content based upon application components -->
      <v-content>
        <overview-grid></overview-grid>
      </v-content>
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
