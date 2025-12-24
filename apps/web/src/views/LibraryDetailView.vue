<script setup lang="ts">
import { useMediaStore } from '@/stores/media'
import { storeToRefs } from 'pinia'
import MediaCard from '@/components/MediaCard.vue'
import { useRouter } from 'vue-router'
const mediaStore = useMediaStore()
const { movies, series } = storeToRefs(mediaStore)
const router = useRouter()

function clickMedia(id: string | undefined) {
  if (id === undefined) return
  router.push({
    name: 'WatchMedia',
    params: {
      mediaId: id,
    },
  })
}
</script>

<template>
  <div
    class="w-full h-full max-h-[calc(100vh-96px)] rounded-lg grid gap-2 grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-5 xl:grid-cols-6 overflow-y-auto"
  >
    <MediaCard
      v-for="m of movies"
      :key="m.id"
      :id="m.id"
      :duration-seconds="m.metadata?.durationSeconds"
      :name="m.info?.name"
      :thumbnail="m.info?.thumbnail"
      :release-date="m.info?.releaseDate"
      @click="() => clickMedia(m.id)"
    />
    <MediaCard
      v-for="s of series"
      :key="s.id"
      :id="s.id"
      :name="s.name"
      :thumbnail="s.thumbnail"
      :release-date="s.releaseDate"
    />
  </div>
</template>
