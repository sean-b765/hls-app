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
  <div class="w-full h-full gap-4 max-h-[calc(100vh-96px)] overflow-y-auto rounded-lg">
    <div class="w-full flex flex-col gap-4">
      <VideoPlayer v-if="selectedMedia" :media="selectedMedia" />

      <!-- Overview -->
      <div
        class="w-full relative flex items-center gap-3 h-80 overflow-hidden rounded-lg bg-muted/70 group"
      >
        <div class="w-full h-full absolute p-2 z-0 duration-200 group-hover:p-0">
          <div
            class="w-full h-full absolute bg-cover bg-center opacity-50 rounded"
            v-if="selectedMedia?.info?.banner"
            :style="{ backgroundImage: `url('${getImage(selectedMedia?.info?.banner)}')` }"
          ></div>
        </div>
        <div class="h-full p-2 min-w-20 thumbnail z-10 duration-200 group-hover:p-0">
          <img
            v-if="selectedMedia?.info?.thumbnail"
            class="h-full w-auto bg-cover rounded-l object-cover"
            :src="getImage(selectedMedia?.info?.thumbnail)"
          />
        </div>
        <div
          class="w-full flex flex-col flex-1 py-2 px-8 truncate z-10 duration-200 group-hover:pl-10 group-hover:py-4"
        >
          <span class="w-full text-wrap font-bold text-2xl mb-10">
            {{ selectedMedia?.info?.name }}
          </span>
          <span class="w-full text-wrap text-sm">{{ selectedMedia?.info?.description }}</span>
        </div>
      </div>
    </div>
  </div>
</template>
