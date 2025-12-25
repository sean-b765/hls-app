import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import { mediaApi, moviesApi, seriesApi } from '@/lib/api.js'
import type { Media, TvSeriesCollection } from '@hls-app/sdk'
import { useRoute } from 'vue-router'

export const useMediaStore = defineStore('media', () => {
  const media = ref<Media[]>([])
  const movies = ref<Media[]>([])
  const series = ref<TvSeriesCollection[]>([])

  const route = useRoute()

  const selectedMedia = computed<Media | undefined>(() => {
    const id = route.params['mediaId'] as string
    const m = media.value.find((el) => el.id === id)
    return m
  })

  async function getMovies() {
    const res = await moviesApi.findMovies()
    movies.value = res.data
  }

  async function getSeries() {
    const res = await seriesApi.findTvSeries()
    series.value = res.data
  }

  async function getMedia(mediaId: string) {
    const res = await mediaApi.getById(mediaId)
    if (res.status !== 200) return

    const found = res.data

    const index = media.value.findIndex((el) => el.id === found.id)
    if (index === -1) {
      media.value.push(found)
    } else {
      media.value.splice(index, 1, found)
    }
  }

  async function getAll() {
    const response = await mediaApi.getAll()
    media.value = response.data
  }

  return {
    media,
    selectedMedia,
    movies,
    series,
    getMovies,
    getSeries,
    getAll,
    getMedia,
  }
})
