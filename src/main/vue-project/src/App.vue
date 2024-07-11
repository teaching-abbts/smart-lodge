<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useTheme } from 'vuetify'

const theme = useTheme()
const drawer = ref<boolean | null>(null)
const appTitle = ref('Firefighter Dashboard')
const appSubtitle = ref('ABB-TS NDS WEG')
const userInfo = ref()

function toggleTheme() {
  theme.global.name.value = theme.global.current.value.dark ? 'light' : 'dark'
}

function logout() {
  location.href = '/logout'
}

async function getUserInfo() {
  userInfo.value = await fetch('/user-info').then((response) => response.json())
}

onMounted(async () => {
  await getUserInfo()
})
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
      <v-list-item
        v-if="userInfo"
        prepend-icon="mdi-account-cowboy-hat"
        :title="userInfo.username"
        subtitle="User"
      />
      <v-btn @click="toggleTheme" icon="mdi-theme-light-dark" />
      <v-btn @click="logout" icon="mdi-logout" />
    </v-app-bar>
    <v-main>
      <v-container>
        <RouterView />
      </v-container>
    </v-main>
  </v-app>
</template>
