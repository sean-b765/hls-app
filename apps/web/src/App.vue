<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

router.afterEach((to, from) => {
  const toDepth = to.path.split('/').filter(Boolean).length
  const fromDepth = from.path.split('/').filter(Boolean).length
  if (toDepth == fromDepth) to.meta.transition = 'fade'
  else to.meta.transition = toDepth < fromDepth ? 'slide-right' : 'slide-left'
})
</script>

<template>
  <RouterView />
</template>
