<script setup lang="ts">
import { Folder, Forward, MoreHorizontal } from 'lucide-vue-next'

import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu'
import {
  SidebarGroup,
  SidebarGroupLabel,
  SidebarMenu,
  SidebarMenuAction,
  SidebarMenuButton,
  SidebarMenuItem,
  useSidebar,
} from '@/components/ui/sidebar'
import { computed } from 'vue'
import { RouterLink } from 'vue-router'

const recentlyWatched = computed<{ name: string; url: string; icon: string }[]>(() => {
  return []
})

const { isMobile } = useSidebar()
</script>

<template>
  <SidebarGroup v-if="recentlyWatched.length" class="group-data-[collapsible=icon]:hidden">
    <SidebarGroupLabel>Recently Watched</SidebarGroupLabel>
    <SidebarMenu>
      <SidebarMenuItem v-for="item in recentlyWatched" :key="item.name">
        <SidebarMenuButton as-child>
          <RouterLink :to="item.url">
            <component :is="item.icon" />
            <span>{{ item.name }}</span>
          </RouterLink>
        </SidebarMenuButton>
        <DropdownMenu>
          <DropdownMenuTrigger as-child>
            <SidebarMenuAction show-on-hover>
              <MoreHorizontal />
              <span class="sr-only">More</span>
            </SidebarMenuAction>
          </DropdownMenuTrigger>
          <DropdownMenuContent
            class="w-48 rounded-lg"
            :side="isMobile ? 'bottom' : 'right'"
            :align="isMobile ? 'end' : 'start'"
          >
            <DropdownMenuItem>
              <Folder class="text-muted-foreground" />
              <span>Remove from Recently Watched</span>
            </DropdownMenuItem>
            <DropdownMenuItem>
              <Forward class="text-muted-foreground" />
              <span>Never show in Recently Watched</span>
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </SidebarMenuItem>
    </SidebarMenu>
  </SidebarGroup>
</template>
