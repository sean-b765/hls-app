import type { Media } from '@/types/media'
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useMediaStore = defineStore('media', () => {
  const media = ref<Media[]>([])

  return { media }
})
