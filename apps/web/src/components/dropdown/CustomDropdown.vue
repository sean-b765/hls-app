<script setup lang="ts">
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu'
import { type LucideIcon } from 'lucide-vue-next'
const { items } = defineProps<{
  items: Array<{ text: string; icon: LucideIcon; onClick: (e: MouseEvent) => void }>
}>()
</script>

<template>
  <DropdownMenu>
    <DropdownMenuTrigger as-child>
      <slot name="trigger" />
    </DropdownMenuTrigger>
    <DropdownMenuContent>
      <DropdownMenuItem
        v-for="item of items"
        :key="item.text"
        class="h-6 flex items-center justify-between"
        :class="{ 'cursor-pointer': !!item.onClick }"
        @click="item.onClick"
      >
        <span class="text-xs">{{ item.text }}</span>
        <div>
          <component :is="item.icon" :size="14" />
        </div>
      </DropdownMenuItem>
    </DropdownMenuContent>
  </DropdownMenu>
</template>
