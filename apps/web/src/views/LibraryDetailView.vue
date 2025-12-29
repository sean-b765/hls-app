<script setup lang="ts">
import { storeToRefs } from 'pinia'
import MediaCard from '@/components/MediaCard.vue'
import { useRouter } from 'vue-router'
import { useLibraryStore } from '@/stores/library'
import HoverCard from '@/components/HoverCard.vue'
import { AlertCircle, ScanSearch } from 'lucide-vue-next'
import Empty from '@/components/ui/empty/Empty.vue'
import EmptyHeader from '@/components/ui/empty/EmptyHeader.vue'
import EmptyMedia from '@/components/ui/empty/EmptyMedia.vue'
import EmptyTitle from '@/components/ui/empty/EmptyTitle.vue'
import EmptyContent from '@/components/ui/empty/EmptyContent.vue'
import Button from '@/components/ui/button/Button.vue'
const libraryStore = useLibraryStore()
const { movies, series, selectedLibrary } = storeToRefs(libraryStore)
const router = useRouter()

function clickMedia(id: string | undefined) {
  if (id === undefined) return
  router.push({
    name: 'WatchMedia',
    params: {
      mediaId: id,
    },
  })
}
</script>

<template>
  <div
    class="w-full h-auto max-h-[calc(100vh-96px)] rounded-lg grid gap-2 grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-5 xl:grid-cols-6 overflow-y-auto"
  >
    <template v-if="!selectedLibrary">
      <Empty class="col-span-full">
        <EmptyHeader>
          <EmptyMedia>
            <AlertCircle />
          </EmptyMedia>
          <EmptyTitle>Not found</EmptyTitle>
        </EmptyHeader>
        <EmptyContent>
          The library you requested was not found.
          <RouterLink to="/libraries">
            <Button variant="link"> Go back </Button>
          </RouterLink>
        </EmptyContent>
      </Empty>
    </template>
    <template v-else>
      <template v-if="movies.length || series.length">
        <MediaCard
          v-for="m of movies"
          :key="m.id"
          :id="m.id"
          :duration-seconds="m.metadata?.durationSeconds"
          :name="m.info?.name"
          :thumbnail="m.info?.thumbnail"
          :release-date="m.info?.releaseDate"
          @click="() => clickMedia(m.id)"
        />
        <MediaCard
          v-for="s of series"
          :key="s.id"
          :id="s.id"
          :name="s.name"
          :thumbnail="s.thumbnail"
          :release-date="s.releaseDate"
        />
      </template>
      <HoverCard v-else @click="() => libraryStore.scan(selectedLibrary!.id)">
        <template #content>
          <p class="text-md cursor-pointer flex items-center gap-2">
            <ScanSearch :size="20" />
            Scan
          </p>
        </template>
      </HoverCard>
    </template>
  </div>
</template>
