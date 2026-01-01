<script setup lang="ts">
import AuthDialog from '@/components/AuthDialog.vue'
import { useUserStore } from '@/stores/user'
import { storeToRefs } from 'pinia'
const userStore = useUserStore()
const { isReady, isLoggedIn } = storeToRefs(userStore)
</script>

<template>
  <AuthDialog :open="isReady && !isLoggedIn" />
  <slot />
</template>
