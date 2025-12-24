<script setup lang="ts">
import AddLibraryDialog from '@/components/AddLibraryDialog.vue'
import HoverCard from '@/components/HoverCard.vue'
import { getLibraryIcon } from '@/lib/utils'
import { useLibraryStore } from '@/stores/libraries'
import { useUserStore } from '@/stores/user'
import { Plus } from 'lucide-vue-next'
import { storeToRefs } from 'pinia'
import { ref } from 'vue'
const libraryStore = useLibraryStore()
const userStore = useUserStore()
const { libraries } = storeToRefs(libraryStore)
const { isAdmin } = storeToRefs(userStore)
const addLibraryDialog = ref(false)
</script>

<template>
  <AddLibraryDialog v-model:open="addLibraryDialog" />
  <div
    class="w-full h-full max-h-[calc(100vh-96px)] rounded-lg overflow-y-auto grid gap-2 grid-cols-1 sm:grid-cols-3"
  >
    <HoverCard v-for="library in libraries" :key="library.id">
      <template #prepend-header>
        <component
          :is="getLibraryIcon(library)"
          :size="130"
          class="absolute opacity-10 transition-all duration-250 group-hover:opacity-20"
          style="left: 50%; top: 50%; transform: translate(-50%, -50%)"
        />
      </template>
      <template #header>
        <p class="text-lg font-bold">{{ library.name }}</p>
      </template>
    </HoverCard>

    <HoverCard v-if="isAdmin" @click="() => (addLibraryDialog = true)">
      <template #content>
        <p class="text-md flex items-center gap-2">
          <Plus :size="20" />
          Create
        </p>
      </template>
    </HoverCard>
  </div>
</template>
