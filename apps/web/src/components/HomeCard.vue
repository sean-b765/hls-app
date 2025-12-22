<!-- 
  File contains the component that displays the featured movie 
  to the user at the top of the Homepage. Basic display has been made, 
  though currents lacks functionality (i.e. buttons)
-->

<script setup lang="ts">
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle,} from '@/components/ui/card'
import { getImage } from '@/lib/utils'
import moment from 'moment'

const { name, releaseDate, description, banner } = defineProps<{
  id?: string
  releaseDate?: string
  name?: string
  description?: string
  banner?: string
}>()
</script>

<template>

  <Card class="flex h-120 max-w-screen">
    <div v-if="banner" class="banner" 
      :style="{backgroundImage: `url('${getImage(banner)}')`, 
    }">
      <div class="overlay">
        <div class="movieInfo">
          <CardHeader >
            <CardTitle class="text-4xl"> {{ name }}</CardTitle>
            <CardDescription>{{ moment(releaseDate).get('year') }}</CardDescription>
          </CardHeader>
          <CardContent>
            <p class="description">{{ description }}</p>
          </CardContent>
          <CardFooter class="gap-5">
            <!-- TODO: Add functionality to buttons.-->
            <button class="button"> Watch now</button> <!-- NOTE: May run into issues where if movie desc is too long, buttons will go off screen. Yet to test if this is true. -->
            <button class="button"> More Info</button>
          </CardFooter>
        </div>
      </div>
    </div>

  </Card>
</template>

<!-- CSS stuff, Very messy atm but will redo it better -->
<style scoped>
  .title {
    display: flex;
    font-size: 48px;
    font-weight: bold;
    padding-left: 0px;
    background-repeat: no-repeat;
  }

  /* Adjusts width and length of the banner */
  .banner {
    background-size: cover;
    height: 130%;
    width: 170%;
    background-position: center;
  }

  /* Adjusts the grey scale overlay  */
  .overlay {
    position: relative;
    height: 100%;
    width: 100%;
    background:
      linear-gradient(
        to bottom,
        var(--color-background) -8%,
         rgba(36, 35, 35, 0.2) 20%,
         rgba(14, 13, 13, 0.4) 60%,
          var(--color-background) 100%
      )
  }

  /* Adjusts variables for movie information  */
  .movieInfo {
    position: relative;
    top: 0;
    height: 100vh;
    max-width: 600px;
    padding: 2rem;

    background:
      linear-gradient(
        to right,
          var(--color-background) 0%,
         rgba(107, 107, 107, 0.2) 85%,
         rgba(46, 45, 45, 0) 98%

      )
  }

  .description{
    font-size: 20px;
    font-weight: bold;
  }

.button {
  padding: 15px 30px;
  color: #ffffff;
  background-color: var(--color-primary);
  border-radius: 5px;
  font-size: 18px;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s ease, box-shadow 0.3s ease;
  margin-top: 40px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.3); 
}

.button:hover {
  background-color: var(--color-chart-5);
}

.button:active {
  transform: scale(0.98); /* Slight press effect on click */
}
</style>