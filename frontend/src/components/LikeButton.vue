<template>
  <div>
      <input type="button" @click="postLike" name="Likes">
      <div>{{numberOfLikes}}</div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { LikeService } from '../services/LikeService';

@Component
export default class LikeButton extends Vue {

    numberOfLikes: Number = 0;

    postLike() {
        let likeService = new LikeService();
        likeService.postLike();
        this.refreshNumberOfLikes();
    }


    setNumberOfLikes(numberOfLikes: object) {
        if (numberOfLikes !== undefined) {
                this.numberOfLikes = numberOfLikes as unknown as number;
            }
    }

    refreshNumberOfLikes() {
        let likeService = new LikeService();
        return likeService.getCount().then(value => this.setNumberOfLikes(value) );
        
    }
}
</script>

<style>

</style>
