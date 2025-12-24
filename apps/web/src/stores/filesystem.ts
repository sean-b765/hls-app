import { ref } from 'vue'
import { defineStore } from 'pinia'
import type { FolderNode } from '@/types/filesystem'
import { fileSystemAPI } from '@/lib/api'

export const useFileSystemStore = defineStore('filesystem', () => {
  const root = { children: [], hasChildren: true, name: 'Root', path: '' }
  const rootFolder = ref<FolderNode>(root)

  async function openFolder(folder: FolderNode = rootFolder.value) {
    const response = await fileSystemAPI.findFolders(folder.path)
    if (response.status !== 200) return

    const children = response.data
    folder.children = children
    folder.hasChildren = children.length !== 0
  }

  return {
    rootFolder,
    openFolder,
  }
})
