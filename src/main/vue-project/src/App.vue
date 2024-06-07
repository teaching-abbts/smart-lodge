<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import FotoAlbum, { type Foto } from '@/components/FotoAlbum.vue'
import FileInput from './components/FileInput.vue'

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
  <div class="controls">
    <div class="control-item">
      <label>Upload:</label>
      <FileInput v-model="uploadFile" :accept="uploadFileFieldAccept" />
      <button :disabled="!uploadFile" @click="onButtonUploadClick">Upload</button>
    </div>
    <div class="control-item">
      <label>Image width:</label>
      <select v-model="imgWidth">
        <option v-for="imgWidth in imgWidths" :key="imgWidth" :value="imgWidth">
          {{ imgWidth }}
        </option>
      </select>
    </div>
    <div class="control-item">
      <label>Filter:</label>
      <input type="text" v-model="filtertext" />
    </div>
  </div>
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
