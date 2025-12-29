<script setup lang="ts">
import { onMounted } from 'vue'
import FolderTree from './FolderTree.vue'
import type { FolderNode } from '@/types/filesystem'
import { useFileSystemStore } from '@/stores/filesystem'
import { storeToRefs } from 'pinia'

const fileSystemStore = useFileSystemStore()
const { rootFolder } = storeToRefs(fileSystemStore)
const selected = defineModel<string>('selected')

async function fetchFolders(node?: FolderNode): Promise<void> {
  await fileSystemStore.openFolder(node)
}

onMounted(async () => {
  await fetchFolders()
})
</script>

<template>
  <div class="border rounded-lg p-3 h-[300px] max-h-[300px] overflow-auto">
    <FolderTree
      v-for="folder in rootFolder.children"
      v-model:selected="selected"
      :key="folder.path"
      :node="folder"
    />
  </div>
</template>
