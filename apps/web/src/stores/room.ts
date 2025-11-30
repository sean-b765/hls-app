import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import { client } from '@/lib/SocketClient'
import type { ChatEvent, ChooseTrackEvent, JoinRoomEvent, RoomCreateEvent } from '@/types/event'
import { PlayerState } from '@/types/room'

export const useRoomStore = defineStore('room', () => {
  const room = ref({
    code: null,
  })
  const playerState = ref<PlayerState>({
    buffering: false,
    loaded: false,
    playing: false,
  })

  const isConnected = computed(() => room.value.code !== null)

  function createRoom(name: string) {
    const payload: RoomCreateEvent = { type: 'ROOM', eventType: 'ROOM_CREATE', roomName: name }
    client.sendJson(payload)
    console.log(payload)
  }

  function joinRoom(roomCode: string) {
    const payload: JoinRoomEvent = { type: 'ROOM', eventType: 'ROOM_JOIN', roomCode }
    client.sendJson(payload)
  }

  function chat(message: string) {
    const payload: ChatEvent = { type: 'CHAT', chat: { message: message, userId: '123123' } }
    client.sendJson(payload)
  }

  function chooseMedia(trackId: string) {
    const payload: ChooseTrackEvent = { eventType: 'CHOOSE_TRACK', type: 'PLAYER', trackId }
    console.log(payload)
    client.sendJson(payload)
  }

  return { room, isConnected, playerState, createRoom, joinRoom, chat, chooseMedia }
})
