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
  <Card class="flex items-end h-80 group relative overflow-hidden cursor-pointer rounded-sm">
    <div class="h-full w-full z-0 absolute transition-all">
      <div
        v-if="thumbnail"
        class="bg-cover bg-center w-full h-full transition-all"
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
    <CardFooter
      class="w-full h-auto bg-muted/50 flex backdrop-blur-lg items-start gap-1 flex-col pb-2 pt-2 px-3 z-1 transition-all duration-300 rounded-none group-hover:bg-muted/90"
    >
      <Tooltip>
        <TooltipTrigger
          as="span"
          class="w-full text-sm truncate opacity-95 font-bold group-hover:opacity-100"
        >
          {{ name }}
        </TooltipTrigger>
        <TooltipContent side="bottom" :side-offset="30">
          <span>{{ name }}</span>
        </TooltipContent>
      </Tooltip>
      <span class="w-full truncate flex justify-between">
        <span class="truncate flex-1 text-xs opacity-90 group-hover:opacity-100">
          {{ moment(releaseDate).get('year') }}
        </span>
        <span class="truncate text-xs opacity-60 group-hover:opacity-70">
          {{ durationSeconds ? formatDuration(durationSeconds) : '' }}
        </span>
      </span>
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
