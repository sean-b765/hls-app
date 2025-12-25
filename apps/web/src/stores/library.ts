import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import type { Library } from '@/types/libraries'
import { libraryApi } from '@/lib/api'
import { useRoute } from 'vue-router'

export const useLibraryStore = defineStore('library', () => {
  const handlers: unknown = {}
  const libraries = ref<Library[]>([])

  const route = useRoute()

  const selectedLibrary = computed<Library | undefined>(() => {
    const id = route.params['libraryId'] as string
    const m = libraries.value.find((el) => el.id === id)
    return m
  })

  function handleIncomingWebSocketEvent(event: unknown) {}

  async function create(library: Library) {
    const response = await libraryApi.create(library)
    if (response.status !== 201) return
    libraries.value.push(response.data)
  }

  async function update(library: Library) {
    const response = await libraryApi.update(library)
    if (response.status !== 200) return
    const updated = response.data
    const index = libraries.value.findIndex((lib) => lib.id === updated.id)
    libraries.value.splice(index, 1, updated)
  }

  async function scan(id: string) {
    await libraryApi.scan(id)
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
    selectedLibrary,
    getAll,
    create,
    update,
    scan,
    deleteById,
    handleIncomingWebSocketEvent,
  }
})
