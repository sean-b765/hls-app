import { ref } from 'vue'
import { defineStore } from 'pinia'
import type { Library } from '@/types/libraries'

export const useLibraryStore = defineStore('library', () => {
  const handlers: unknown = {}
  const libraries = ref<Library[]>([])

  function handleIncomingWebSocketEvent(event: unknown) {}

  return {
    libraries,
    handleIncomingWebSocketEvent,
  }
})
