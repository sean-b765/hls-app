<script setup lang="ts">
import AddLibraryDialog from '@/components/AddLibraryDialog.vue'
import Button from '@/components/ui/button/Button.vue'
import Card from '@/components/ui/card/Card.vue'
import CardContent from '@/components/ui/card/CardContent.vue'
import CardHeader from '@/components/ui/card/CardHeader.vue'
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
    <Card v-for="library in libraries" :key="library.id" class="h-80 flex flex-col">
      <CardHeader class="pb-0">
        <p class="text-sm">{{ library.name }}</p>
      </CardHeader>
      <CardContent class="flex grow items-center justify-center"> </CardContent>
    </Card>
    <Card class="h-80 flex flex-col">
      <CardHeader class="pb-0">
        <p v-if="libraries.length === 0" class="text-sm opacity-80">You have no libraries.</p>
      </CardHeader>
      <CardContent class="flex grow items-center justify-center">
        <Button
          v-if="isAdmin"
          class="cursor-pointer"
          size="sm"
          variant="ghost"
          @click="() => (addLibraryDialog = true)"
        >
          <Plus />
          Create
        </Button>
      </CardContent>
    </Card>
  </div>
</template>
