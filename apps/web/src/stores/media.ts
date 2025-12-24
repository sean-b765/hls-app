import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import { mediaApi, moviesApi, seriesApi } from '@/lib/api.js'
import type { Media, TvSeriesCollection } from '@hls-app/sdk'
import type { Progress, ScanProgress } from '@/types/media'
import { useRoute } from 'vue-router'

export const useMediaStore = defineStore('media', () => {
  const media = ref<Media[]>([])
  const movies = ref<Media[]>([])
  const series = ref<TvSeriesCollection[]>([])
  const scanProgress = ref<ScanProgress>({})

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

  async function progressUpdate(mediaId: string, progress: Progress) {
    scanProgress.value[mediaId] = progress
    if (progress !== 'READY') return
    await getMedia(mediaId)
  }

  function handleScanProgressEvent(update: ScanProgress) {
    for (const mediaId of Object.keys(update)) {
      const oldStatus = scanProgress.value[mediaId]
      if (oldStatus === 'READY') continue

      const newStatus = update[mediaId]
      if (!newStatus || newStatus === oldStatus) continue

      progressUpdate(mediaId, newStatus)
    }
  }

  async function getAll() {
    const response = await mediaApi.getAll()
    media.value = response.data
  }

  async function startScanProgress() {
    const events = new EventSource(`${import.meta.env.VITE_BASE_URL}/api/media/scan-progress`)
    events.addEventListener('progress', (e: MessageEvent) => {
      const data: ScanProgress = JSON.parse(e.data)
      handleScanProgressEvent(data)
    })
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
    startScanProgress,
    scanProgress,
  }
})
