<script setup lang="ts">
import AppSidebar from '@/components/AppSidebar.vue'
import RoomProvider from '@/components/RoomProvider.vue'
import HeaderBar from '@/components/HeaderBar.vue'
import { SidebarInset, SidebarProvider } from '@/components/ui/sidebar'
import AuthProvider from '@/components/AuthProvider.vue'
</script>

<template>
  <AuthProvider>
    <RoomProvider>
      <SidebarProvider>
        <AppSidebar />
        <SidebarInset>
          <div class="flex flex-1 flex-col p-4 pt-0">
            <HeaderBar />
            <RouterView v-slot="{ Component, route }">
              <div class="relative flex-1">
                <transition :name="(route.meta.transition as string) || 'fade'">
                  <component :is="Component" :key="route.path" class="absolute" />
                </transition>
              </div>
            </RouterView>
          </div>
        </SidebarInset>
      </SidebarProvider>
    </RoomProvider>
  </AuthProvider>
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
