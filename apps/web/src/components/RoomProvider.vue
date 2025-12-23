<script setup lang="ts">
import { watch } from 'vue'
import { client } from '@/lib/SocketClient'
import { useRoomStore } from '@/stores/room'
import { generateUID } from '@/lib/utils'

const { createRoom } = useRoomStore()

watch(
  () => client.state.connected,
  (v) => {
    if (!v) return
    createRoom(generateUID())
  },
)
</script>

<template>
  <slot />
</template>
