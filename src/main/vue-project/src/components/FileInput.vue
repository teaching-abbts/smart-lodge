<script setup lang="ts">
defineProps({
  modelValue: {
    required: true,
    type: File
  },
  accept: {
    required: false,
    type: String
  }
})

const emit = defineEmits(['update:modelValue'])

function onFileInputChange(event: Event) {
  const inputElement = event.target as HTMLInputElement

  if (inputElement && inputElement.files) {
    const file = inputElement.files[0]

    if (file) {
      emit('update:modelValue', file)
    }
  }
}
</script>

<template>
  <input ref="input" type="file" :value="modelValue" :accept="accept" @change="onFileInputChange" />
</template>
