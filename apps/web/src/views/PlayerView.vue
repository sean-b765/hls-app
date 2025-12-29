<script setup lang="ts">
import VideoPlayer from '@/components/VideoPlayer.vue'
import { getImage } from '@/lib/utils'
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
  <div class="w-full h-full gap-4 max-h-[calc(100vh-96px)] overflow-y-auto rounded-md">
    <div class="w-full flex flex-col gap-4">
      <div class="w-full flex flex-col flex-1 truncate z-10">
        <span class="w-full text-wrap font-bold text-xl ml-2 mt-2">
          {{ selectedMedia?.info?.name }}
        </span>
      </div>
      <VideoPlayer v-if="selectedMedia" :media="selectedMedia" />

      <!-- Overview -->
      <div class="w-full relative flex gap-3 h-100 overflow-hidden rounded-lg bg-muted/70">
        <div class="w-full h-full absolute z-0 box-border">
          <div
            class="w-full h-full absolute bg-cover p-2 bg-center opacity-50 rounded bg-breathe"
            v-if="selectedMedia?.info?.banner"
            :style="{ backgroundImage: `url('${getImage(selectedMedia?.info?.banner)}')` }"
          ></div>
        </div>
        <div class="h-full p-2 min-w-20 thumbnail z-10 box-border">
          <img
            v-if="selectedMedia?.info?.thumbnail"
            class="h-full w-auto bg-cover rounded object-cover"
            :src="getImage(selectedMedia?.info?.thumbnail)"
          />
        </div>
        <div class="w-full flex flex-col flex-1 py-2 pr-8 truncate z-10">
          <span class="w-full text-wrap text-sm text-shadow-xs text-shadow-black">
            {{ selectedMedia?.info?.description }}
          </span>
        </div>
      </div>
    </div>
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
