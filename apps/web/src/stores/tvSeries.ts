import { seriesApi } from '@/lib/api'
import { defineStore } from 'pinia'
import { useCrudStore } from './crudStore'
import type { TvSeriesCollection } from '@hls-app/sdk'

export const useTvSeriesStore = defineStore('tvSeries', () => {
  const crud = useCrudStore<TvSeriesCollection>({ api: seriesApi })

  return {
    ...crud,
  }
})
