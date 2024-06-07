<script setup lang="ts">
import { computed, ref, watch } from 'vue'

const props = defineProps({
  modelValue: {
    required: true,
    default: undefined,
    type: [File, Array<File>]
  },
  accept: {
    required: false,
    type: String
  }
})

const emit = defineEmits(['update:modelValue'])
const input = ref<HTMLInputElement>()
const modelValueWrapper = computed(() => props.modelValue)

watch(
  modelValueWrapper,
  (newVal) => {
    if (newVal === undefined && input.value) {
      input.value.value = ''
    }
  },
  {
    immediate: true
  }
)

function onFileInputChange(event: Event) {
  const inputElement = event.target as HTMLInputElement
  const files = inputElement.files

  if (files) {
    if (files.length === 0) {
      emit('update:modelValue', null)
    } else if (files.length === 1) {
      emit('update:modelValue', files[0])
    } else {
      emit('update:modelValue', Array.from(files))
    }
  }
}
</script>

<template>
  <input ref="input" type="file" :accept="props.accept" @change="onFileInputChange" />
</template>
