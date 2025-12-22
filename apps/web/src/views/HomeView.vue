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
import { onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import { computed } from 'vue'

const mediaStore = useMediaStore()
const { movies, series } = storeToRefs(mediaStore)

onMounted(async () => {
  await mediaStore.getMovies()
  await mediaStore.getSeries()
  // await mediaStore.startScanProgress()
})

// Example featured movie ID is 'Inside Out', choosing featured movie will eventually be automated.
const featuredMovieID = "3a914481-22a4-4124-9d98-654cb2f30c45"
const featuredMovie = computed(() => 
  movies.value.find(m => m.id === featuredMovieID) 
)

// testing carousel with movies, will eventually create backend functionality 
const recommendedMovies = computed(() =>
  movies.value.slice(2, 12)
)

const topMovies = computed(() =>
  movies.value.slice(0, 10)
)

const topSeries = computed(() =>
  series.value.slice(0, 10)
)

</script>
<template>
  <div>
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
  <div class="carousel"> 
    <HomeCarousel 
      :listType="'Recommended'"
      :mediaList="recommendedMovies" 
      />
    <HomeCarousel 
      :listType="'Top 10 Movies'"
      :mediaList="topMovies"
    />
    <HomeCarousel 
      :listType="'Top 10 Series'"
      :mediaList="topSeries"
    />
  </div>
</template>

<style scoped>
  .carousel {
    margin-top: 1px;
  }
</style>
