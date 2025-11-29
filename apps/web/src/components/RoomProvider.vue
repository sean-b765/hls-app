<script setup lang="ts">
import { onMounted } from 'vue'
import { client } from '@/lib/SocketClient'
import { useRoomStore } from '@/stores/room'

const { createRoom } = useRoomStore()

onMounted(() => {
  client.connect()
  createRoom('1234')
})
</script>

<template>
  <slot />
</template>
