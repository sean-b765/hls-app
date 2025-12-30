<script setup lang="ts">
import AddLibraryDialog from '@/components/AddLibraryDialog.vue'
import LilCircleButton from '@/components/button/LilCircleButton.vue'
import CustomDropdown from '@/components/dropdown/CustomDropdown.vue'
import EditLibraryDialog from '@/components/EditLibraryDialog.vue'
import HoverCard from '@/components/HoverCard.vue'
import { getLibraryIcon } from '@/lib/utils'
import { useLibraryStore } from '@/stores/library'
import { useUserStore } from '@/stores/user'
import { Library } from '@hls-app/sdk'
import { Ellipsis, Navigation, Pencil, Plus, ScanSearch, Trash } from 'lucide-vue-next'
import { storeToRefs } from 'pinia'
import { ref } from 'vue'
import { useRouter } from 'vue-router'
const libraryStore = useLibraryStore()
const userStore = useUserStore()
const { items: libraries } = storeToRefs(libraryStore)
const { isAdmin } = storeToRefs(userStore)
const addLibraryDialog = ref(false)
const editLibraryDialog = ref(false)
const editingLibrary = ref<Library | null>(null)

const router = useRouter()

function openEditDialog(library: Library) {
  editingLibrary.value = library
  editLibraryDialog.value = true
}
</script>

<template>
  <div
    class="w-full h-auto max-h-[calc(100vh-96px)] rounded-md overflow-y-auto grid gap-2 grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5"
  >
    <AddLibraryDialog v-model:open="addLibraryDialog" />
    <EditLibraryDialog
      v-if="editingLibrary"
      v-model:open="editLibraryDialog"
      v-model:library="editingLibrary"
    />

    <HoverCard v-for="library in libraries" :key="library.id" header-class="justify-between">
      <template #prepend-header>
        <component
          :is="getLibraryIcon(library)"
          :size="130"
          class="absolute opacity-10 transition-all duration-250 group-hover:opacity-20"
          style="left: 50%; top: 50%; transform: translate(-50%, -50%)"
        />
      </template>
      <template #header>
        <RouterLink :to="`/libraries/${library.id}`">
          <p class="text-lg font-bold">{{ library.name }}</p>
        </RouterLink>

        <CustomDropdown
          :items="[
            {
              text: 'View',
              icon: Navigation,
              onClick: () => router.push(`/libraries/${library.id}`),
            },
            {
              text: 'Edit',
              icon: Pencil,
              onClick: () => openEditDialog(library),
            },
            {
              text: 'Scan',
              icon: ScanSearch,
              onClick: () => libraryStore.scan(library.id),
            },
            {
              text: 'Delete',
              icon: Trash,
              onClick: () => libraryStore.deleteById(library.id),
            },
          ]"
        >
          <template #trigger>
            <LilCircleButton>
              <Ellipsis :size="14" />
            </LilCircleButton>
          </template>
        </CustomDropdown>
      </template>
    </HoverCard>

    <HoverCard v-if="isAdmin" class="cursor-pointer" @click="() => (addLibraryDialog = true)">
      <template #content>
        <p class="text-md flex items-center gap-2">
          <Plus :size="20" />
          Create
        </p>
      </template>
    </HoverCard>
  </div>
</template>
