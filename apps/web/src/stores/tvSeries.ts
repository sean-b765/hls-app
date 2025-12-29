import { seriesApi } from '@/lib/api'
import { defineStore } from 'pinia'
import { useCrudStore } from './crudStore'
import type { TvSeries } from '@hls-app/sdk'

export const useTvSeriesStore = defineStore('tvSeries', () => {
  const crud = useCrudStore<TvSeries>({ api: seriesApi })

  return {
    ...crud,
  }
})
