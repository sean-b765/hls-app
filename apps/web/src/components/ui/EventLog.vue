<template>
  <div>
    <div class="flex flex-col">
      SENT
      <div v-for="(event, i) in sent" :key="i" class="max-h-80 overflow-auto">
        <VueJsonPretty :data="event" :collapsed-node-length="4" show-length />
      </div>
    </div>
    <div class="flex flex-col">
      RECEIVED
      <div v-for="(event, i) in received" :key="i" class="max-h-80 overflow-auto">
        <VueJsonPretty
          :data="event"
          :collapsed-node-length="4"
          show-length
          @node-click="clickNode"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import VueJsonPretty from 'vue-json-pretty'
import { useEventStore } from '@/stores/event'
import 'vue-json-pretty/lib/styles.css'
import type { NodeDataType } from 'vue-json-pretty/types/components/TreeNode'
import { useRoomStore } from '@/stores/room'

const { sent, received } = useEventStore()
const { chooseMedia } = useRoomStore()

function clickNode(e: NodeDataType) {
  if (!e.content || !e.content.toString().startsWith('file://')) return
  chooseMedia(e.content.toString())
}
</script>
