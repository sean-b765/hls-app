<script setup lang="ts">
import VideoPlayer from '@/components/VideoPlayer.vue'
import { useMediaStore } from '@/stores/media'
import { Media } from '@hls-app/sdk'
import { storeToRefs } from 'pinia'
import { computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'

const mediaStore = useMediaStore()
const { media } = storeToRefs(mediaStore)
const { params } = useRoute()

const selectedMedia = computed<Media | undefined>(() => {
  const id = params['mediaId'] as string
  const m = media.value.find((el) => el.id === id)
  return m
})

onMounted(async () => {
  await mediaStore.getMedia(params['mediaId'] as string)
})
</script>

<template>
  <div class="w-full h-full flex flex-col gap-3">
    <VideoPlayer v-if="selectedMedia" :media="selectedMedia" />
    <div class="w-full flex">
      {{ selectedMedia?.info?.name }}
    </div>
  </div>
</template>
