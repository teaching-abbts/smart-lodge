<script setup lang="ts">
import { ref, onMounted } from 'vue'
const fotoalbum = ref()

onMounted(() => {
  fetch('/fotoalbum')
    .then((response) => response.json())
    .then((payload) => (fotoalbum.value = payload))
})
</script>

<template>
  <div class="fotoalbum" v-if="fotoalbum">
    <img v-for="foto in fotoalbum.fotos" :key="foto.url" :src="foto.url" :alt="foto.name" />
  </div>
</template>

<style scoped>
.fotoalbum {
  display: flex;
  flex-direction: row;
}

img {
  width: 400px;
}
</style>
