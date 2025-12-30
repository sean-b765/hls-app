<script setup lang="ts">
import RoomProvider from '@/components/RoomProvider.vue'
import AuthProvider from '@/components/AuthProvider.vue'
</script>

<template>
  <AuthProvider>
    <RoomProvider>
      <div class="relative min-h-svh bg-background flex flex-1 flex-col">
        <RouterView v-slot="{ Component, route }">
          <div class="relative flex-1">
            <transition :name="(route.meta.transition as string) || 'fade'">
              <component :is="Component" :key="route.path" class="absolute" />
            </transition>
          </div>
        </RouterView>
      </div>
    </RoomProvider>
  </AuthProvider>
</template>
