<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import FotoAlbum, { type Foto } from '@/components/Fotoalbum.vue'

const fotoalbum = ref<{ fotos: Foto[] }>()
const filtertext = ref()
const imgWidth = ref<string>('200px')
const uploadFile = ref<File | Array<File>>()
const uploadFileFieldAccept = '.jpg'

const fotos = computed(() => {
  let response: Foto[] | undefined = fotoalbum.value?.fotos

  if (filtertext.value) {
    response = fotoalbum.value?.fotos?.filter((f) => f.name.includes(filtertext.value))
  }

  return response
})

const imgWidths = computed(() => {
  const response = []

  for (let i = 1; i <= 10; i++) {
    response.push(`${i * 100}px`)
  }

  return response
})

function fetchFotoalbumFromServer() {
  fetch('/fotoalbum')
    .then((response) => response.json())
    .then((payload) => (fotoalbum.value = payload))
}

async function onButtonUploadClick() {
  if (uploadFile.value) {
    const formData = new FormData()

    formData.append('uploadFile', uploadFile.value as File)

    const response = await fetch('/upload-images', {
      method: 'POST',
      body: formData
    })

    if (!response.ok) {
      alert(response.statusText)
    } else {
      uploadFile.value = undefined
      fetchFotoalbumFromServer()
    }
  }
}

onMounted(fetchFotoalbumFromServer)
</script>

<template>
  <v-container>
    <v-row>
      <v-col>
        <v-file-input label="File input" v-model="uploadFile" :accept="uploadFileFieldAccept" />
      </v-col>
      <v-col>
        <v-btn :disabled="!uploadFile" @click="onButtonUploadClick" size="x-large"> Upload </v-btn>
      </v-col>
      <v-col>
        <v-select label="Image width:" v-model="imgWidth" :items="imgWidths" />
      </v-col>
      <v-col>
        <v-text-field label="Filter:" v-model="filtertext" />
      </v-col>
    </v-row>
  </v-container>
  <FotoAlbum :fotos="fotos" :imgWidth="imgWidth" />
</template>

<style scoped>
.controls {
  display: flex;
  flex-direction: row;
  justify-content: center;
}

.control-item {
  margin: 10px;
}

label {
  margin-right: 10px;
}
</style>
