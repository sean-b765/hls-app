<!--
  File contains the component that displays the featured movie
  to the user at the top of the Homepage. Basic display has been made,
  though currents lacks functionality (i.e. buttons)
-->

<script setup lang="ts">
import { Button } from '@/components/ui/button'
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from '@/components/ui/card'
import { getImage } from '@/lib/utils'
import { useColorMode } from '@vueuse/core'
import { truncate } from 'lodash'
import moment from 'moment'

const { name, releaseDate, description, banner } = defineProps<{
  id?: string
  releaseDate?: string
  name?: string
  description?: string
  banner?: string
}>()
const mode = useColorMode()
</script>

<template>
  <Card
    class="home-card flex flex-col flex-1 w-full max-w-full relative rounded-lg overflow-hidden"
  >
    <div
      v-if="banner"
      class="banner w-full h-full z-0 rounded-lg scale-105"
      :style="{ backgroundImage: `url('${getImage(banner)}')` }"
    >
      <div v-if="mode === 'dark'" class="overlay rounded-lg"></div>
    </div>
    <CardHeader class="z-10">
      <CardTitle class="text-4xl font-bold text-white">
        {{ name }}
      </CardTitle>
      <CardDescription class="max-w-1/3 text-white">
        {{ moment(releaseDate).get('year') }}
      </CardDescription>
    </CardHeader>
    <CardContent class="z-10">
      <p
        class="description text-white text-shadow-md text-shadow-black opacity-85"
        style="max-width: clamp(200px, 50%, 600px)"
      >
        {{ truncate(description, { length: 200 }) }}
      </p>
    </CardContent>
    <CardFooter class="mt-5 gap-5 z-10">
      <!-- TODO: Add functionality to buttons.-->
      <Button class="button">Watch now</Button>
      <!-- NOTE: May run into issues where if movie desc is too long, buttons will go off screen. Yet to test if this is true. -->
      <Button variant="secondary" class="button">More Info</Button>
    </CardFooter>
  </Card>
</template>

<!-- CSS stuff, Very messy atm but will redo it better -->
<style scoped lang="scss">
.home-card {
  height: 70vh;
}
.title {
  font-size: 3rem;
  font-weight: bold;
}

/* Adjusts width and length of the banner */
.banner {
  overflow: hidden;
  position: absolute;
  background-repeat: no-repeat;
  background-size: cover;
  background-position: top center;
  width: 100%;
  will-change: transform;
  animation: bg-breathe 40s ease-in-out infinite;

  /* Adjusts the grey scale overlay  */
  .overlay {
    position: absolute;
    height: 100%;
    width: 100%;
    background: linear-gradient(
      to bottom,
      rgba(14, 13, 13, 0.3) 0%,
      rgba(0, 0, 0, 0.5) 50%,
      var(--color-background) 99%
    );
  }
}

.description {
  font-size: 0.9rem;
}

.button {
  cursor: pointer;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.3);
  transition: transform 0.1s ease-in-out;
}

.button:active {
  transform: scale(0.98); /* Slight press effect on click */
}

@keyframes bg-breathe {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.04);
  }
  100% {
    transform: scale(1);
  }
}
</style>
