import { ref } from 'vue'
import { defineStore } from 'pinia'
import type { Library } from '@/types/libraries'
import { libraryApi } from '@/lib/api'

export const useLibraryStore = defineStore('library', () => {
  const handlers: unknown = {}
  const libraries = ref<Library[]>([])

  function handleIncomingWebSocketEvent(event: unknown) {}

  async function create(library: Library) {
    const response = await libraryApi.create(library)
    if (response.status !== 201) return
    libraries.value.push(response.data)
  }

  async function getAll() {
    const response = await libraryApi.getAll()
    if (response.status !== 200) return
    libraries.value = response.data
  }

  async function deleteById(id: string) {
    const response = await libraryApi.deleteById(id)
    if (response.status !== 200) return
    libraries.value = libraries.value.filter((el) => el.id !== id)
  }

  return {
    libraries,
    getAll,
    create,
    deleteById,
    handleIncomingWebSocketEvent,
  }
})
