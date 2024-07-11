<script setup lang="ts">
import { onBeforeUnmount, onMounted, ref } from 'vue'

type SmartHome = { buildingID: string }
type Measurement = {
  timeStamp: string // Format: YYYY-MM-DD hh:mm:ss
  buildingID: string
  brightness: number
  temperature: number
  humidity: number
  gas: number
}

let intervalId: number | null = null
const smartHomes = ref<SmartHome[]>([])
const measurements = ref<Measurement[]>([])

async function getSmartHomes() {
  smartHomes.value = await fetch('/get-smart-homes').then((response) => response.json())
}

async function getMeasurements() {
  measurements.value = await fetch('/get-smart-home-measurements').then((response) =>
    response.json()
  )
}

async function loadData() {
  await getSmartHomes()
  await getMeasurements()
}

onMounted(async () => {
  await loadData()
  intervalId = setInterval(loadData, 10000)
})

onBeforeUnmount(() => {
  if (intervalId) {
    clearInterval(intervalId)
  }
})
</script>

<template>
  <v-container>
    <v-row>
      <v-col v-for="smartHome in smartHomes" :key="smartHome.buildingID" cols="4">
        <v-card height="200">
          <v-card-title>{{ smartHome.buildingID }}</v-card-title>
          <v-card-actions>
            <v-dialog max-width="500">
              <template v-slot:activator="{ props: activatorProps }">
                <v-btn
                  v-bind="activatorProps"
                  color="surface-variant"
                  text="Measurements"
                  variant="flat"
                ></v-btn>
              </template>
              <template v-slot:default="{ isActive }">
                <v-card title="Dialog">
                  <v-data-table
                    :items="measurements.filter((m) => m.buildingID === smartHome.buildingID)"
                  ></v-data-table>
                  <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn text="Close Dialog" @click="isActive.value = false"></v-btn>
                  </v-card-actions>
                </v-card>
              </template>
            </v-dialog>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>
