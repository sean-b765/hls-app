<!--
  File contains the structure and components for the Homepage.
  The homepage is made up of the following components:
    -> HomeCard.vue
    -> HomeCarousel.vue
    -> HomeNavbar.vue (Not being used currently by the Homepage.)
-->

<script setup lang="ts">
import HomeCarousel from '@/components/HomeCarousel.vue'
import HomeCard from '@/components/HomeCard.vue'
// import HomeNavbar from '@/components/HomeNavbar.vue'

import { useMediaStore } from '@/stores/media'
import { storeToRefs } from 'pinia'
import { computed } from 'vue'
import { useTvSeriesStore } from '@/stores/tvSeries'

const mediaStore = useMediaStore()
const tvSeriesStore = useTvSeriesStore()
const { movies } = storeToRefs(mediaStore)
const { items: series } = storeToRefs(tvSeriesStore)

// Featured movie is a random media for now
const featuredMovie = computed(() =>
  movies.value.at(Math.floor(Math.random() * movies.value.length)),
)

// testing carousel with movies, will eventually create backend functionality
const recommendedMovies = computed(() =>
  Array.isArray(movies.value) ? movies.value.slice(2, 12) : [],
)

const topMovies = computed(() => (Array.isArray(movies.value) ? movies.value.slice(0, 10) : []))

const topSeries = computed(() => (Array.isArray(series.value) ? series.value.slice(0, 10) : []))
</script>
<template>
  <div class="w-full h-full max-h-[calc(100vh-96px)] rounded-lg overflow-y-auto flex flex-col">
    <div class="w-full h-auto">
      <!-- HomeCard displays a featured movie on the homescreen, right now it is labled as 'Continue Watching and only displays one movie atm.'-->
      <HomeCard
        v-if="featuredMovie"
        :key="featuredMovie.id"
        :id="featuredMovie.id"
        :name="featuredMovie.info?.name"
        :thumbnail="featuredMovie.info?.thumbnail"
        :banner="featuredMovie.info?.banner"
        :description="featuredMovie.info?.description"
        :releaseDate="featuredMovie.info?.releaseDate"
      />
    </div>
    <div>
      <!-- Navbar option like the one in dribbble example, HomePage currently has netflix style arrangement. -->
      <!-- <HomeNavbar /> -->
    </div>
    <div class="w-full">
      <HomeCarousel
        :listType="'Recommended'"
        :mediaList="recommendedMovies.map((m) => ({ id: m.id, thumbnail: m.info?.thumbnail }))"
      />
      <HomeCarousel
        :listType="'Top 10 Movies'"
        :mediaList="topMovies.map((m) => ({ id: m.id, thumbnail: m.info?.thumbnail }))"
      />
      <HomeCarousel
        :listType="'Top 10 Series'"
        :mediaList="topSeries.map((s) => ({ id: s.id, thumbnail: s.thumbnail }))"
      />
    </div>
  </div>
</template>
