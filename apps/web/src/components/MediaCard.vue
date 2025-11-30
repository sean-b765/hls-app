<script setup lang="ts">
import { Card, CardFooter } from '@/components/ui/card'
import { Tooltip, TooltipContent, TooltipTrigger } from '@/components/ui/tooltip'
import { formatSeconds } from '@/lib/utils'
import { useMediaStore } from '@/stores/media'
import { Progress } from '@/types/media'
import { Media } from '@hls-app/sdk'
import { storeToRefs } from 'pinia'

const { media } = defineProps<{ media: Media }>()
const mediaStore = useMediaStore()
const { scanProgress } = storeToRefs(mediaStore)
function progress(media: Media): Progress {
  if (scanProgress.value[media.id as string]) return scanProgress.value[media.id as string]
  if (media.metadata && media.info) return 'READY'
  if (!media.metadata) return 'METADATA'
  return 'INFO'
}
</script>

<template>
  <Card
    class="flex items-end h-68 group relative overflow-hidden border-2 border-card cursor-pointer"
  >
    <div class="h-full w-full z-0 p-1 absolute transition-all group-hover:p-0">
      <div
        v-if="media.info?.thumbnail"
        class="bg-cover bg-center w-full h-full transition-all duration-150 rounded-lg group-hover:rounded-xl"
        :style="{
          backgroundImage: `url('https://image.tmdb.org/t/p/w1280${media.info?.thumbnail}')`,
        }"
      ></div>
      <div v-else class="w-full h-full flex items-center justify-center">
        <span
          class="loader mb-16"
          :style="{
            animationDelay: `${Math.random() * 500}ms`,
            animationDuration: `${750 + Math.random() * 500}ms`,
          }"
        ></span>
      </div>
    </div>
    <CardFooter class="w-full p-1 pt-0 transition-all duration-150 group-hover:p-0">
      <div
        class="w-full h-full bg-muted/90 flex backdrop-blur-lg items-start rounded-b-lg gap-2 flex-col py-2 px-4 z-1 transition-all duration-150 group-hover:bg-muted/80 group-hover:pb-3 group-hover:px-5"
      >
        <Tooltip>
          <TooltipTrigger as="div" class="w-full truncate">
            <span class="w-full truncate text-sm opacity-70 group-hover:opacity-100">
              {{ progress(media) }}
              {{ media.info?.name }}
            </span>
          </TooltipTrigger>
          <TooltipContent>
            <span>{{ media.info?.name }}</span>
          </TooltipContent>
        </Tooltip>
        <span class="w-full truncate flex justify-between">
          <span class="truncate text-xs opacity-50 group-hover:opacity-80">
            {{ formatSeconds(media.metadata?.durationSeconds) }}
          </span>
          <span class="truncate text-xs opacity-50 group-hover:opacity-80">
            {{ media.info?.releaseDate }}
          </span>
        </span>
      </div>
    </CardFooter>
  </Card>
</template>

<style scoped>
.loader {
  width: 24px;
  height: 24px;
  border: 5px solid var(--color-card-foreground);
  border-bottom-color: var(--color-primary);
  border-radius: 50%;
  display: inline-block;
  box-sizing: border-box;
  animation: rotation 1s ease-in-out infinite;
  opacity: 0.75;
}

@keyframes rotation {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>
