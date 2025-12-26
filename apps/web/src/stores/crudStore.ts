import type { CrudAPI } from '@/lib/api'
import { computed, ref, type Ref } from 'vue'

export interface Identifiable {
  id?: string
}

export interface CrudStoreOptions<T extends Identifiable> {
  api: CrudAPI<T>
}

export function useCrudStore<T extends Identifiable>({ api }: CrudStoreOptions<T>) {
  const items: Ref<T[]> = ref<T[]>([]) as Ref<T[]>

  function upsertMutation(...payload: T[]) {
    for (const item of payload) {
      const existingIndex = items.value.findIndex((i) => i.id === item.id)
      if (existingIndex === -1) items.value.push(item)
      else items.value[existingIndex] = item
    }
  }

  function removeMutation(id: string) {
    const indexOfId = items.value.findIndex((i) => i.id === id)
    if (indexOfId === -1) return
    items.value.splice(indexOfId, 1)
  }

  function getById(id: string) {
    return computed(() => items.value.find((i) => i.id === id))
  }

  async function findById(id: string) {
    try {
      const res = await api.findById(id)
      if (res.status !== 200) return
      upsertMutation(res.data)
    } catch {}
  }

  async function findAll() {
    try {
      const res = await api.findAll()
      if (res.status !== 200) return
      upsertMutation(...res.data)
    } catch {}
  }

  async function upsert(item: T) {
    try {
      const res = await api.upsert(item)
      if (res.status !== 200) return
      upsertMutation(res.data)
    } catch {}
  }

  async function create(item: T) {
    try {
      const res = await api.create(item)
      if (res.status !== 201) return
      upsertMutation(res.data)
    } catch {}
  }

  async function deleteById(id: string) {
    try {
      const res = await api.deleteById(id)
      if (res.status !== 200) return
      removeMutation(id)
    } catch {}
  }

  return {
    items,
    getById,
    findAll,
    findById,
    create,
    upsert,
    deleteById,
  }
}
