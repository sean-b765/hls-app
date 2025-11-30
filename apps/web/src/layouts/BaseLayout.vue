<script setup lang="ts">
import AppSidebar from '@/components/AppSidebar.vue'
import ClientProvider from '@/components/RoomProvider.vue'
import HeaderBar from '@/components/HeaderBar.vue'
import { SidebarInset, SidebarProvider } from '@/components/ui/sidebar'
</script>

<template>
  <ClientProvider>
    <SidebarProvider>
      <AppSidebar />
      <SidebarInset>
        <div class="flex flex-1 flex-col p-4 pt-0">
          <HeaderBar />
          <RouterView />
        </div>
      </SidebarInset>
    </SidebarProvider>
  </ClientProvider>
</template>
