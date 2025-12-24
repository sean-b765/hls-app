<script setup lang="ts">
import AuthDialog from '@/components/AuthDialog.vue'
import { useLibraryStore } from '@/stores/libraries'
import { useUserStore } from '@/stores/user'
import { storeToRefs } from 'pinia'
import { watch } from 'vue'

const userStore = useUserStore()
const libraryStore = useLibraryStore()
const { isReady, isLoggedIn } = storeToRefs(userStore)

watch(
  () => [isReady, isLoggedIn],
  async ([ready, authed]) => {
    if (!ready.value) return
    if (!authed.value) return

    // User is ready and authenticated
    await libraryStore.getAll()
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
