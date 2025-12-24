<script setup lang="ts">
import { ChevronRight, LibrarySquare } from 'lucide-vue-next'
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from '@/components/ui/collapsible'
import {
  SidebarGroup,
  SidebarGroupLabel,
  SidebarMenu,
  SidebarMenuAction,
  SidebarMenuButton,
  SidebarMenuItem,
  SidebarMenuSub,
  SidebarMenuSubButton,
  SidebarMenuSubItem,
} from '@/components/ui/sidebar'
import { useLibraryStore } from '@/stores/libraries'
import { storeToRefs } from 'pinia'

const libraryStore = useLibraryStore()
const { libraries } = storeToRefs(libraryStore)

const items = [
  {
    title: 'Libraries',
    url: '/libraries',
    icon: LibrarySquare,
    isActive: true,
  },
]
</script>

<template>
  <SidebarGroup>
    <SidebarGroupLabel>Media</SidebarGroupLabel>
    <SidebarMenu>
      <Collapsible v-for="item in items" :key="item.title" as-child :default-open="item.isActive">
        <SidebarMenuItem>
          <SidebarMenuButton as-child :tooltip="item.title">
            <RouterLink :to="item.url">
              <component :is="item.icon" />
              <span>{{ item.title }}</span>
            </RouterLink>
          </SidebarMenuButton>
          <CollapsibleTrigger as-child v-if="libraries.length">
            <SidebarMenuAction class="data-[state=open]:rotate-90">
              <ChevronRight />
              <span class="sr-only">Toggle</span>
            </SidebarMenuAction>
          </CollapsibleTrigger>
          <CollapsibleContent class="mr-0">
            <SidebarMenuSub>
              <SidebarMenuSubItem v-for="library in libraries" :key="library.id">
                <SidebarMenuSubButton as-child>
                  <RouterLink :to="`/libraries/${library.id}`">
                    <span>{{ library.name }}</span>
                  </RouterLink>
                </SidebarMenuSubButton>
              </SidebarMenuSubItem>
            </SidebarMenuSub>
          </CollapsibleContent>
        </SidebarMenuItem>
      </Collapsible>
    </SidebarMenu>
  </SidebarGroup>
</template>
