<script setup lang="ts">
import Card from '@/components/ui/card/Card.vue'
import CardFooter from '@/components/ui/card/CardFooter.vue'
import { useMediaStore } from '@/stores/media'
import { formatSeconds } from '@/lib/utils'
import { storeToRefs } from 'pinia'
import { onMounted } from 'vue'
const mediaStore = useMediaStore()
const { media } = storeToRefs(mediaStore)

onMounted(async () => {
  await mediaStore.getAll()
})
</script>

<template>
  <div class="w-full flex gap-2 flex-wrap">
    <Card
      v-for="m of media"
      :key="m.id"
      class="max-w-xs flex items-end aspect-video h-68 group relative overflow-hidden border-2 border-card"
    >
      <div class="h-full w-full z-0 p-1 absolute transition-all group-hover:p-0">
        <div
          class="bg-cover bg-center w-full h-full transition-all duration-150 rounded-lg group-hover:rounded-xl"
          style="
            background-image: url(https://images-cdn.ubuy.co.in/68901f6ae49b8c404602f009-the-batman-movie-poster-glossy-quality.jpg);
          "
        ></div>
      </div>
      <CardFooter class="w-full p-1 pt-0 transition-all duration-150 group-hover:p-0">
        <div
          class="w-full h-full bg-muted/90 flex backdrop-blur-lg items-start rounded-b-lg gap-2 flex-col py-2 px-4 z-1 transition-all duration-150 group-hover:bg-muted/80 group-hover:pb-3 group-hover:px-5"
        >
          <span class="w-full truncate text-sm">{{ m.path?.split(/[\\/]/g).at(-1) }}</span>
          <span class="w-full truncate text-xs">
            {{ formatSeconds(m.metadata?.durationSeconds) }}
          </span>
        </div>
      </CardFooter>
    </Card>
  </div>
</template>
