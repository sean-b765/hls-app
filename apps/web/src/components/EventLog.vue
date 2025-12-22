<template>
  <div>
    <div class="flex flex-col">
      Outgoing
      <div v-for="(event, i) in sent" :key="i" class="max-h-80 overflow-auto">
        <VueJsonPretty :data="event" :collapsed-node-length="4" show-length />
      </div>
    </div>
    <div class="flex flex-col">
      Incoming
      <div v-for="(event, i) in received" :key="i" class="max-h-80 overflow-auto">
        <VueJsonPretty :data="event" :collapsed-node-length="4" show-length />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import VueJsonPretty from 'vue-json-pretty'
import { useEventStore } from '@/stores/event'
import 'vue-json-pretty/lib/styles.css'

const { sent, received } = useEventStore()
</script>
