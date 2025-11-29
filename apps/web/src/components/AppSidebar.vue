<script setup lang="ts">
import type { SidebarProps } from '@/components/ui/sidebar'

import { CloudCheck, CloudOff, LayoutGrid } from 'lucide-vue-next'

import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarHeader,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from '@/components/ui/sidebar'
import NavLibraries from '@/components/NavLibraries.vue'
import NavRecentlyWatched from '@/components/NavRecentlyWatched.vue'
import NavUser from '@/components/NavUser.vue'
import { client } from '@/lib/SocketClient'
import { Tooltip, TooltipContent, TooltipTrigger } from '@/components/ui/tooltip'
import { Button } from '@/components/ui/button'

const props = withDefaults(defineProps<SidebarProps>(), {
  variant: 'inset',
})

const data = {
  user: {
    name: 'anonymous',
    email: '',
    avatar: 'test.png',
  },
}
</script>

<template>
  <Sidebar v-bind="props" class="shadow-md/50">
    <SidebarHeader>
      <SidebarMenu>
        <SidebarMenuItem>
          <SidebarMenuButton size="lg" as-child>
            <RouterLink to="/">
              <div
                class="flex aspect-square size-8 items-center justify-center rounded-lg bg-sidebar-primary text-sidebar-primary-foreground"
              >
                <LayoutGrid class="size-4" />
              </div>
              <div class="grid flex-1 text-left text-sm leading-tight ml-2">
                <span class="flex justify-between truncate font-medium">
                  <span>Home</span>
                  <Tooltip>
                    <TooltipTrigger>
                      <Button
                        :disabled="client.state.connected"
                        class="rounded-full cursor-pointer"
                        size="icon"
                        variant="outline"
                        @click="() => client.connect()"
                      >
                        <CloudCheck
                          v-if="client.state.connected"
                          :size="10"
                          class="text-green-600"
                        />
                        <CloudOff v-else :size="10" class="text-amber-600" />
                      </Button>
                    </TooltipTrigger>
                    <TooltipContent>
                      {{ client.state.connected ? 'Connected to server' : 'Connect to server' }}
                    </TooltipContent>
                  </Tooltip>
                </span>
                <span class="truncate text-xs opacity-75"> </span>
              </div>
            </RouterLink>
          </SidebarMenuButton>
        </SidebarMenuItem>
      </SidebarMenu>
    </SidebarHeader>
    <SidebarContent>
      <NavLibraries />
      <NavRecentlyWatched />
    </SidebarContent>
    <SidebarFooter>
      <NavUser :user="data.user" />
    </SidebarFooter>
  </Sidebar>
</template>
