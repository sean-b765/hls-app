<script setup lang="ts">
import { useFileSystemStore } from '@/stores/filesystem'
import { FolderNode } from '@/types/filesystem'
import { ChevronDown, ChevronRight, Folder } from 'lucide-vue-next'

const { node } = defineProps<{
  node: FolderNode
}>()

const selected = defineModel<string>('selected')

const fileSystemStore = useFileSystemStore()

async function toggle(node: FolderNode) {
  if (!node.hasChildren) return

  node.expanded = !node.expanded

  if (node.expanded) {
    node.loading = true
    await fileSystemStore.openFolder(node)
    node.loading = false
  }
}
</script>

<template>
  <div>
    <div
      class="flex items-center rounded px-2 gap-2 cursor-pointer py-1 select-none"
      :class="{ 'bg-muted': selected === node.path }"
      @click="() => (selected = node.path)"
      @dblclick="toggle(node)"
    >
      <button v-if="node.hasChildren" class="w-4 text-xs" @click.stop="toggle(node)">
        <ChevronDown v-if="node.expanded" :size="14" />
        <ChevronRight v-else :size="14" />
      </button>

      <span v-else class="w-4"></span>

      <Folder :size="16" />
      <span class="text-sm">{{ node.name }}</span>
    </div>

    <div v-if="node.loading" class="ml-6 text-xs text-muted-foreground">Loading...</div>

    <template v-if="node.expanded">
      <FolderTree
        v-for="child in node.children"
        v-model:selected="selected"
        :key="child.path"
        class="ml-4"
        :node="child"
      />
    </template>
  </div>
</template>
