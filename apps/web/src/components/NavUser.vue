<script setup lang="ts">
import { BadgeCheck, ChevronsUpDown, Cog, LogOut, Moon, Sun } from 'lucide-vue-next'

import { Avatar, AvatarFallback } from '@/components/ui/avatar'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu'
import {
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  useSidebar,
} from '@/components/ui/sidebar'
import { useColorMode } from '@vueuse/core'
import { useUserStore } from '@/stores/user'
import { storeToRefs } from 'pinia'

const { isMobile } = useSidebar()
const mode = useColorMode()
const userStore = useUserStore()
const { username, highestRole, usernameShort } = storeToRefs(userStore)
</script>

<template>
  <SidebarMenu>
    <SidebarMenuItem>
      <DropdownMenu>
        <DropdownMenuTrigger as-child>
          <SidebarMenuButton
            size="lg"
            class="data-[state=open]:bg-sidebar-accent data-[state=open]:text-sidebar-accent-foreground"
          >
            <Avatar class="h-8 w-8 rounded-lg">
              <!-- <AvatarImage :src="user.avatar" :alt="user.name" /> -->
              <AvatarFallback class="rounded-lg">
                {{ usernameShort }}
              </AvatarFallback>
            </Avatar>
            <div class="grid flex-1 text-left text-sm leading-tight">
              <span class="truncate font-medium">{{ username }}</span>
              <span class="truncate text-xs">{{ highestRole }}</span>
            </div>
            <ChevronsUpDown class="ml-auto size-4" />
          </SidebarMenuButton>
        </DropdownMenuTrigger>
        <DropdownMenuContent
          class="w-[--reka-dropdown-menu-trigger-width] min-w-56 rounded-lg"
          :side="isMobile ? 'bottom' : 'right'"
          align="end"
          :side-offset="4"
        >
          <DropdownMenuLabel class="p-0 font-normal">
            <div class="flex items-center gap-2 px-1 py-1.5 text-left text-sm">
              <Avatar class="h-8 w-8 rounded-lg">
                <!-- <AvatarImage :src="user.avatar" :alt="user.name" /> -->
                <AvatarFallback class="rounded-lg">
                  {{ usernameShort }}
                </AvatarFallback>
              </Avatar>
              <div class="grid flex-1 text-left text-sm leading-tight">
                <span class="truncate font-semibold">{{ username }}</span>
                <span class="truncate text-xs">{{ highestRole }}</span>
              </div>
            </div>
          </DropdownMenuLabel>
          <DropdownMenuSeparator />
          <DropdownMenu>
            <DropdownMenuTrigger as-child>
              <DropdownMenuGroup>
                <DropdownMenuItem>
                  <Sun v-if="mode === 'light'" />
                  <Moon v-else-if="mode === 'dark'" />
                  <span>
                    Theme:
                    <span v-if="mode === 'dark'">Dark</span>
                    <span v-else-if="mode === 'light'">Light</span>
                  </span>
                </DropdownMenuItem>
              </DropdownMenuGroup>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end">
              <DropdownMenuItem @click="mode = 'auto'"> System </DropdownMenuItem>
              <DropdownMenuItem v-if="mode !== 'dark'" @click="mode = 'dark'">
                Dark
              </DropdownMenuItem>
              <DropdownMenuItem v-if="mode !== 'light'" @click="mode = 'light'">
                Light
              </DropdownMenuItem>
            </DropdownMenuContent>
          </DropdownMenu>
          <DropdownMenuSeparator />
          <DropdownMenuGroup>
            <DropdownMenuItem>
              <BadgeCheck />
              Account
            </DropdownMenuItem>
            <DropdownMenuItem>
              <Cog />
              Settings
            </DropdownMenuItem>
          </DropdownMenuGroup>
          <DropdownMenuSeparator />
          <DropdownMenuItem @click="userStore.signout">
            <LogOut />
            Log out
          </DropdownMenuItem>
        </DropdownMenuContent>
      </DropdownMenu>
    </SidebarMenuItem>
  </SidebarMenu>
</template>
