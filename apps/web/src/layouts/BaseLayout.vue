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
            <RouterView />
          </div>
        </SidebarInset>
      </SidebarProvider>
    </RoomProvider>
  </AuthProvider>
</template>
