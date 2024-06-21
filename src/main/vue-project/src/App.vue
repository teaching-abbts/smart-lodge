<script setup lang="ts">
import { ref } from 'vue'
import { useTheme } from 'vuetify'

const theme = useTheme()
const drawer = ref<boolean | null>(null)
const appTitle = ref('Firefighter Dashboard')
const appSubtitle = ref('ABB-TS NDS WEG')

function toggleTheme() {
  theme.global.name.value = theme.global.current.value.dark ? 'light' : 'dark'
}
</script>

<template>
  <v-app>
    <v-navigation-drawer v-model="drawer">
      <v-list-item :title="appTitle" :subtitle="appSubtitle" />
      <v-divider />
      <v-list-item link to="/">Home</v-list-item>
      <v-list-item link to="/fotoalbum">Fotoalbum</v-list-item>
    </v-navigation-drawer>
    <v-app-bar>
      <v-app-bar-nav-icon @click="drawer = !drawer"></v-app-bar-nav-icon>
      <v-app-bar-title>{{ appTitle }}</v-app-bar-title>
      <v-btn @click="toggleTheme" icon="mdi-theme-light-dark" />
    </v-app-bar>
    <v-main>
      <v-container>
        <RouterView />
      </v-container>
    </v-main>
  </v-app>
</template>

<style scoped></style>
