<script setup lang="ts">
import { useMediaStore } from '@/stores/media'
import { storeToRefs } from 'pinia'
import { onMounted } from 'vue'
import MediaCard from '@/components/MediaCard.vue'
const mediaStore = useMediaStore()
const { media } = storeToRefs(mediaStore)

onMounted(async () => {
  await mediaStore.getAll()
  await mediaStore.startScanProgress()
})
</script>

<template>
  <div
    class="w-full h-full max-h-[calc(100vh-96px)] rounded-lg grid gap-2 grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-5 xl:grid-cols-6 overflow-y-auto"
  >
    <MediaCard v-for="m of media" :key="m.id" :media="m" />
  </div>
</template>
