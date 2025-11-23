<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { client } from './lib/SocketClient'
import { useRoomStore } from './stores/room'
import TextButton from '@/components/TextButton.vue'
import TextInput from '@/components/TextInput.vue'
import EventLog from '@/components/ui/EventLog.vue'
import VideoPlayer from '@/components/VideoPlayer.vue'

const roomNameOrCode = ref('')
const message = ref('')
const { isConnected, createRoom, joinRoom, chat } = useRoomStore()

onMounted(() => {
  client.connect()
  createRoom('1234')
})
</script>

<template>
  <div class="flex flex-col">
    <VideoPlayer />
    <!-- Controls -->
    <div class="flex flex-col">
      <div class="w-full">
        <TextButton v-if="!client.state.connected" @click="() => client.connect()">
          Connect
        </TextButton>
        <TextButton v-else @click="() => client.disconnect()">Disconnect</TextButton>
      </div>
      <div v-if="client.state.connected" class="w-full">
        <template v-if="!isConnected">
          <TextButton @click="() => createRoom(roomNameOrCode)">CREATE ROOM</TextButton>
          <TextButton @click="() => joinRoom(roomNameOrCode)">JOIN ROOM</TextButton>
          <TextInput
            v-model:model-value="roomNameOrCode"
            label="Room Name or Code"
            placeholder="Room"
            type="text"
          />
        </template>
      </div>
      <div v-if="client.state.connected" class="w-full">
        <TextButton @click="() => chat(message)">SEND CHAT</TextButton>
        <TextInput
          v-model:model-value="message"
          label="Message"
          placeholder="Your message"
          type="text"
        />
      </div>
      <EventLog class="grid grid-cols-2 gap-2 mx-2" />
    </div>
  </div>
</template>

<style scoped></style>
