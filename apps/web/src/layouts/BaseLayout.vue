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
        <div class="flex flex-1 flex-col gap-4 p-4 pt-0">
          <HeaderBar />
          <RouterView />
          <div class="grid auto-rows-min gap-4 md:grid-cols-3">
            <div class="bg-muted/50 aspect-video rounded-xl" />
            <div class="bg-muted/50 aspect-video rounded-xl" />
            <div class="bg-muted/50 aspect-video rounded-xl" />
          </div>
          <div class="bg-muted/50 min-h-[100vh] flex-1 rounded-xl md:min-h-min" />
        </div>
      </SidebarInset>
    </SidebarProvider>
  </ClientProvider>
</template>
