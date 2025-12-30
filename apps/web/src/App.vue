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
  <RouterView v-slot="{ Component, route }">
    <div class="relative flex-1">
      <transition :name="(route.meta.transition as string) || 'fade'">
        <component :is="Component" :key="route.path" class="absolute" />
      </transition>
    </div>
  </RouterView>
</template>

<style>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* LEFT (forward) */
.slide-left-enter-active,
.slide-left-leave-active,
.slide-right-enter-active,
.slide-right-leave-active {
  transition:
    transform 0.25s ease,
    opacity 0.25s ease;
  transform-origin: center;
}

/* Forward */
.slide-left-enter-from {
  transform: translateX(20px) scale(0.98);
  opacity: 0;
}

.slide-left-leave-to {
  transform: translateX(-20px) scale(0.98);
  opacity: 0;
}

/* Backward */
.slide-right-enter-from {
  transform: translateX(-20px) scale(0.98);
  opacity: 0;
}

.slide-right-leave-to {
  transform: translateX(20px) scale(0.98);
  opacity: 0;
}
</style>
