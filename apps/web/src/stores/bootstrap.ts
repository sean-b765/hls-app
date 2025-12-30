import { defineStore } from 'pinia'
import { useLibraryStore } from '@/stores/library'
import { useMediaStore } from '@/stores/media'
import { useTvSeriesStore } from '@/stores/tvSeries'
import { useUserStore } from '@/stores/user'
import { storeToRefs } from 'pinia'
import { watchEffect } from 'vue'

// Fetch necessary things here for application to function
export const useBootstrapStore = defineStore('bootstrap', () => {
  const userStore = useUserStore()
  const libraryStore = useLibraryStore()
  const mediaStore = useMediaStore()
  const tvSeriesStore = useTvSeriesStore()
  const { isReady, isLoggedIn } = storeToRefs(userStore)

  watchEffect(async () => {
    if (!isReady.value || !isLoggedIn.value) return

    // User is ready and authenticated
    await libraryStore.findAll()
    await mediaStore.findAll()
    await tvSeriesStore.findAll()
  })

  function reset() {
    libraryStore.$reset()
    mediaStore.$reset()
    tvSeriesStore.$reset()
  }

  return {
    reset,
  }
})
