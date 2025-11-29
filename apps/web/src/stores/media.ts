import { defineStore } from 'pinia'
import { ref } from 'vue'
import { mediaApi } from '@/lib/api.js'
import type { Media } from '@hls-app/sdk'

export const useMediaStore = defineStore('media', () => {
  const media = ref<Media[]>([])

  async function getAll() {
    const request = await mediaApi.getAll()
    const response = await request()
    media.value = response.data
  }

  return { media, getAll }
})
