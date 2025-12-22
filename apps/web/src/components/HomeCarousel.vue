<!--
  File contains the component that displays the movie carousels.
  There are currently three carousels, one for recommended movies,
  top 10 movies in the users library, and top 10 series in the user
  library. Prototype of the carousel is complete, though currently
  lacking functionality (i.e. carouses uses preset list of movies
  located in HomeView.vue).
-->

<script setup lang="ts">
import { Card, CardContent } from '@/components/ui/card'
import { Carousel, CarouselContent, CarouselItem } from '@/components/ui/carousel'
import { useSidebar } from '@/components/ui/sidebar'
import { getImage } from '@/lib/utils'
import { Media } from '@hls-app/sdk'
import { computed } from 'vue'

const { listType, mediaList } = defineProps<{
  listType: string
  mediaList: Media[]
}>()
const { open } = useSidebar()
const maxWidth = computed(() => {
  const paddingOffset = 40
  if (open.value) return `calc(100vw - var(--sidebar-width) - ${paddingOffset}px)`
  return `calc(100vw - ${paddingOffset}px)`
})

// const MAX_CAROUSEL_ITEMS = 10 // Max amount of items the carousel contains, may need
</script>
<template>
  <div class="title mt-10">{{ listType }}</div>
  <div class="relative w-full">
    <!-- The max-width should be fixed and depends on the sidebar open state -->
    <Carousel
      class="carousel w-full transition-all"
      :opts="{
        align: 'start',
        dragFree: true,
      }"
      :style="{ maxWidth }"
    >
      <CarouselContent>
        <CarouselItem
          v-for="m in mediaList"
          :key="m.id"
          class="basis-1 md:basis-1/2 lg:basis-1/4 xl:basis-1/6"
        >
          <div class="card">
            <Card>
              <CardContent
                class="card-content"
                :style="{ backgroundImage: `url('${getImage(m.info?.thumbnail ?? '')}')` }"
              >
              </CardContent>
            </Card>
          </div>
        </CarouselItem>
      </CarouselContent>
    </Carousel>
  </div>
</template>

<!-- CSS stuff, Very messy atm but will redo it better -->
<style scoped>
.title {
  display: flex;
  font-size: 24px;
  font-weight: bold;
  padding: 20px;
  padding-left: 0px;
}
.card {
  overflow: hidden;
  border-radius: 0.6rem;
  filter: brightness(100%);
  transition: filter 0.3s ease;
}
.card:hover {
  cursor: pointer;
  border-radius: 0.6rem; /* TODO: Fix issue where border corners are not rounded when hovered over. */
  transform: scale(0.98);
  transition: transform 0.5s ease;
  filter: brightness(90%);
}
.card-content {
  background-size: cover;
  background-position: center;
  width: 100%;
  aspect-ratio: 1/1;
  display: flex;
  padding: 1.5rem;
  height: 22.5rem;
}
</style>
