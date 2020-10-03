<template>
  <div>
    <v-list-item>
      <v-list-item-content>
        <v-list-item-title class="title">
          DimeDiary
        </v-list-item-title>
      </v-list-item-content>
    </v-list-item>

    <v-divider></v-divider>

    <v-list
        dense
        nav
    >
      <v-list-item
          v-for="item in items"
          :key="item.title"
          link
      >
        <v-list-item-icon>
          <v-icon>{{ item.icon }}</v-icon>
        </v-list-item-icon>

        <v-list-item-content>
          <router-link :to="item.path">{{ item.title }}</router-link>
        </v-list-item-content>
      </v-list-item>
    </v-list>
  </div>
</template>

<script lang="ts">


import {Component, Vue} from "vue-property-decorator";


@Component
export default class LeftNavigation extends Vue {


  get items(): Item[] {
    return [new Item("Dashboard", "mdi-view-dashboard", "/"),
      new Item("Transaktionsübersicht", "polymer", "/transactions"),
      new Item("Kontenübersicht", "account_balance", "/bankaccounts")]
  }

}

class Item {

  private _title: string;
  private _icon: string;
  private _path: string;


  constructor(title: string, icon: string, path: string) {
    this._title = title;
    this._icon = icon;
    this._path = path;
  }


  get title(): string {
    return this._title;
  }

  get icon(): string {
    return this._icon;
  }


  get path(): string {
    return this._path;
  }
}

</script>

<style scoped>

</style>