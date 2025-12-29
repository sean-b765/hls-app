<script setup lang="ts">
import AuthDialog from '@/components/AuthDialog.vue'
import { useLibraryStore } from '@/stores/library'
import { useMediaStore } from '@/stores/media'
import { useTvSeriesStore } from '@/stores/tvSeries'
import { useUserStore } from '@/stores/user'
import { storeToRefs } from 'pinia'
import { watch } from 'vue'

const userStore = useUserStore()
const libraryStore = useLibraryStore()
const mediaStore = useMediaStore()
const tvSeriesStore = useTvSeriesStore()
const { isReady, isLoggedIn } = storeToRefs(userStore)

watch(
  () => [isReady, isLoggedIn],
  async ([ready, authed]) => {
    // if (!ready.value) return
    // if (!authed.value) return

    // User is ready and authenticated
    await libraryStore.findAll()
    await mediaStore.findAll()
    await tvSeriesStore.findAll()
  },
  {
    immediate: true,
  },
)
</script>

<template>
  <AuthDialog :open="isReady && !isLoggedIn" />
  <slot />
</template>
