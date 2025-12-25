<script setup lang="ts">
import { Card, CardFooter } from '@/components/ui/card'
import { Tooltip, TooltipContent, TooltipTrigger } from '@/components/ui/tooltip'
import { formatDuration, getImage } from '@/lib/utils'
import moment from 'moment'

const { thumbnail, durationSeconds, name, releaseDate } = defineProps<{
  thumbnail?: string
  id?: string
  releaseDate?: string
  name?: string
  durationSeconds?: number
}>()
</script>

<template>
  <Card
    class="flex items-end h-68 group relative overflow-hidden border-2 border-card cursor-pointer"
  >
    <div class="h-full w-full z-0 p-1 absolute transition-all group-hover:p-0">
      <div
        v-if="thumbnail"
        class="bg-cover bg-center w-full h-full transition-all duration-150 rounded-lg group-hover:rounded-xl"
        :style="{
          backgroundImage: `url('${getImage(thumbnail)}')`,
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
            <span class="w-full truncate text-sm opacity-70 font-bold group-hover:opacity-100">
              {{ name }}
            </span>
          </TooltipTrigger>
          <TooltipContent side="bottom" :side-offset="30">
            <span>{{ name }}</span>
          </TooltipContent>
        </Tooltip>
        <span class="w-full truncate flex justify-between">
          <span class="truncate flex-1 text-xs opacity-50 group-hover:opacity-80">
            {{ durationSeconds ? formatDuration(durationSeconds) : '' }}
          </span>
          <span class="truncate text-xs opacity-50 group-hover:opacity-80">
            {{ moment(releaseDate).get('year') }}
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
  border: 5px solid var(--color-muted);
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
