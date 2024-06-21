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
  <v-container>
    <v-row v-if="fotos">
      <v-img
        v-for="foto in fotos"
        :key="foto.url"
        :src="foto.url"
        :alt="foto.name"
        :width="imgWidth"
        class="mr-2 mb-2"
      />
    </v-row>
    <v-row v-else>
      <v-col>
        <h1>No Content</h1>
      </v-col>
    </v-row>
  </v-container>
</template>
