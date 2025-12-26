import { computed } from 'vue'
import { defineStore } from 'pinia'
import type { Library } from '@/types/libraries'
import { libraryApi } from '@/lib/api'
import { useRoute } from 'vue-router'
import { useCrudStore } from './crudStore'

export const useLibraryStore = defineStore('library', () => {
  const crud = useCrudStore({ api: libraryApi })
  const handlers: unknown = {}

  const route = useRoute()

  const selectedLibrary = computed<Library | undefined>(() => {
    const id = route.params['libraryId'] as string
    const m = crud.items.value.find((el) => el.id === id)
    return m
  })

  function handleIncomingWebSocketEvent(event: unknown) {}

  async function scan(id: string) {
    await libraryApi.scan(id)
  }

  return {
    ...crud,
    scan,
    selectedLibrary,
    handleIncomingWebSocketEvent,
  }
})
