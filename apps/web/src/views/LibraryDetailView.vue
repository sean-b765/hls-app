<script setup lang="ts">
import Card from '@/components/ui/card/Card.vue'
import CardFooter from '@/components/ui/card/CardFooter.vue'
import { useMediaStore } from '@/stores/media'
import { formatSeconds } from '@/lib/utils'
import { storeToRefs } from 'pinia'
import { onMounted } from 'vue'
import Tooltip from '@/components/ui/tooltip/Tooltip.vue'
import TooltipTrigger from '@/components/ui/tooltip/TooltipTrigger.vue'
import TooltipContent from '@/components/ui/tooltip/TooltipContent.vue'
const mediaStore = useMediaStore()
const { media } = storeToRefs(mediaStore)

onMounted(async () => {
  await mediaStore.getAll()
})
</script>

<template>
  <div
    class="w-full h-full max-h-[calc(100vh-96px)] rounded-lg grid gap-2 grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-5 xl:grid-cols-6 overflow-y-auto"
  >
    <Card
      v-for="m of media"
      :key="m.id"
      class="flex items-end h-68 group relative overflow-hidden border-2 border-card cursor-pointer"
    >
      <div class="h-full w-full z-0 p-1 absolute transition-all group-hover:p-0">
        <div
          class="bg-cover bg-center w-full h-full transition-all duration-150 rounded-lg group-hover:rounded-xl"
          :style="{
            backgroundImage: `url('https://image.tmdb.org/t/p/w1280${m.info?.thumbnail}')`,
          }"
        ></div>
      </div>
      <CardFooter class="w-full p-1 pt-0 transition-all duration-150 group-hover:p-0">
        <div
          class="w-full h-full bg-muted/90 flex backdrop-blur-lg items-start rounded-b-lg gap-2 flex-col py-2 px-4 z-1 transition-all duration-150 group-hover:bg-muted/80 group-hover:pb-3 group-hover:px-5"
        >
          <Tooltip>
            <TooltipTrigger as="div" class="w-full truncate">
              <span class="w-full truncate text-sm opacity-70 group-hover:opacity-100">
                {{ m.info?.name }}
              </span>
            </TooltipTrigger>
            <TooltipContent>
              <span>{{ m.info?.name }}</span>
            </TooltipContent>
          </Tooltip>
          <span class="w-full truncate flex justify-between">
            <span class="truncate text-xs opacity-50 group-hover:opacity-80">
              {{ formatSeconds(m.metadata?.durationSeconds) }}
            </span>
            <span class="truncate text-xs opacity-50 group-hover:opacity-80">
              {{ m.info?.releaseDate }}
            </span>
          </span>
        </div>
      </CardFooter>
    </Card>
  </div>
</template>
