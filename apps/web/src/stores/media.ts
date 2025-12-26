import { computed } from 'vue'
import { mediaApi } from '@/lib/api.js'
import type { Media } from '@hls-app/sdk'
import { useRoute } from 'vue-router'
import { useCrudStore } from './crudStore'
import { defineStore } from 'pinia'

export const useMediaStore = defineStore('media', () => {
  const crud = useCrudStore<Media>({ api: mediaApi })

  const movies = computed(() => {
    return crud.items.value.filter((m) => m.info && !m.info.season)
  })
  const series = computed(() => {
    return crud.items.value.filter((m) => m.info && !!m.info.season)
  })

  const route = useRoute()

  const selectedMedia = computed<Media | undefined>(() => {
    const id = route.params['mediaId'] as string
    const m = crud.items.value.find((el) => el.id === id)
    return m
  })

  return {
    ...crud,
    selectedMedia,
    movies,
    series,
  }
})
