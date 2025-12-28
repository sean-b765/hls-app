import { computed } from 'vue'
import { defineStore, storeToRefs } from 'pinia'
import { libraryApi } from '@/lib/api'
import { useRoute } from 'vue-router'
import { useCrudStore } from './crudStore'
import type { Library, Media, TvSeriesCollection } from '@hls-app/sdk'
import { useTvSeriesStore } from './tvSeries'
import { useMediaStore } from './media'

export const useLibraryStore = defineStore('library', () => {
  const crud = useCrudStore<Library>({ api: libraryApi })
  const tvSeriesStore = useTvSeriesStore()
  const mediaStore = useMediaStore()
  const { items: tvSeries } = storeToRefs(tvSeriesStore)
  const { movies: allMovies } = storeToRefs(mediaStore)
  const handlers: unknown = {}

  const route = useRoute()

  const selectedLibrary = computed<Library | undefined>(() => {
    const id = route.params['libraryId'] as string
    const m = crud.items.value.find((el) => el.id === id)
    return m
  })

  const movies = computed<Media[]>(() =>
    allMovies.value.filter((movie) => movie.library?.id === selectedLibrary.value?.id),
  )
  const series = computed<TvSeriesCollection[]>(() =>
    tvSeries.value.filter((tv) => tv.library?.id === selectedLibrary.value?.id),
  )

  function handleIncomingWebSocketEvent(event: unknown) {}

  async function scan(id: string) {
    await libraryApi.scan(id)
  }

  return {
    ...crud,
    scan,
    selectedLibrary,
    movies,
    series,
    handleIncomingWebSocketEvent,
  }
})
