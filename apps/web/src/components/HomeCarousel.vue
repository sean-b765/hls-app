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
import { Carousel, CarouselContent, CarouselItem, CarouselNext, CarouselPrevious } from '@/components/ui/carousel'
import { getImage } from '@/lib/utils'

const { listType, mediaList } = defineProps<{
  listType: string
  mediaList: {
    id?: string
    info?: {
      name?: string
      thumbnail?: string
    }
  }[]
}>()

// const MAX_CAROUSEL_ITEMS = 10 // Max amount of items the carousel contains, may need 

</script>
<template>
  <div class="relative top-20">
    <div class="title">{{ listType }}</div>
    <Carousel
      class="carousel"
      :opts="{
        align: 'start',
      }"
    >
      <CarouselContent>
        <CarouselItem v-for="m in mediaList" 
          :key="m.id" 
          class="md:basis-1/2 lg:basis-1/6 "
        >
          <div class="card">  
            <Card>
              <CardContent class="card-content"
                :style="{backgroundImage: `url('${getImage(m.info?.thumbnail ?? '')}')`,}"
              >
              </CardContent>
            </Card>
          </div>
        </CarouselItem>
      </CarouselContent>
      <CarouselPrevious />
      <CarouselNext />
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
  .carousel {
    display: flex;
    width: 100%;
    max-width: 80vw;
    left: 40px;
  }
  .card {
    overflow: hidden;
    border-radius: 0.6rem;
    filter: brightness(100%);
    transition: filter 0.3s ease;
  }
  .card:hover{
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