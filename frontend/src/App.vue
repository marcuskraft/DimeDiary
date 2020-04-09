<template>
  <div id="app">
    <LikeButton></LikeButton>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import LikeButton from './components/LikeButton.vue';

@Component({
  components: {
    LikeButton: LikeButton
  },
})
export default class App extends Vue {}
</script>

<style>

</style>
