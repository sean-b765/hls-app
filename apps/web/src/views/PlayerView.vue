<script setup lang="ts">
import VideoPlayer from '@/components/VideoPlayer.vue'
import { useMediaStore } from '@/stores/media'
import { storeToRefs } from 'pinia'
import { onMounted } from 'vue'
import { useRoute } from 'vue-router'

const mediaStore = useMediaStore()
const { selectedMedia } = storeToRefs(mediaStore)
const { params } = useRoute()

onMounted(async () => {
  await mediaStore.findById(params['mediaId'] as string)
})
</script>

<template>
  <div class="w-full h-svh">
    <VideoPlayer v-if="selectedMedia" :media="selectedMedia" />
  </div>
</template>

<style scoped>
.bg-breathe {
  animation: bg-breathe 40s ease-in-out infinite;
}
@keyframes bg-breathe {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.04);
  }
  100% {
    transform: scale(1);
  }
}
</style>
