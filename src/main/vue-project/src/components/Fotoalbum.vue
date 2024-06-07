<script lang="ts" setup>
import type { PropType } from 'vue'

export type Foto = { url: string; name: string }

defineProps({
  fotos: {
    default: null,
    required: false,
    type: Array as PropType<Foto[]>
  },
  imgWidth: {
    default: '800px',
    required: false,
    type: String
  }
})
</script>

<template>
  <div v-if="fotos" class="fotoalbum">
    <div v-for="foto in fotos" :key="foto.url" class="foto">
      <img :alt="foto.name" :src="foto.url" />
      <span>{{ foto.name }}</span>
    </div>
  </div>
  <div v-else>
    <h1>No Content</h1>
  </div>
</template>

<style scoped>
.fotoalbum {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: flex-start;
  row-gap: 16px;
  column-gap: 16px;
}

.foto {
  display: flex;
  flex-direction: column;
  align-items: center;
}

img {
  width: v-bind(imgWidth);
  box-shadow: 3px 3px 5px 6px #ccc;
}
</style>
