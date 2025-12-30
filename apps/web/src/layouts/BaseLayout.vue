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
