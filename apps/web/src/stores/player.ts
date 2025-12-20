import { ref } from 'vue'
import { defineStore } from 'pinia'
import { client } from '@/lib/SocketClient'
import {
  type PlayerSeekCommand,
  EventType,
  PlayerCommand,
  type PlayerSeekedEvent,
} from '@/types/messages'
import type { PlayerState } from '@/types/room'

export const usePlayerStore = defineStore('player', () => {
  const handlers: unknown = {}
  const state = ref<PlayerState>({
    buffering: false,
    loaded: false,
    playing: false,
  })

  function handleIncomingWebSocketEvent(event: unknown) {}

  function seeked(evt: PlayerSeekedEvent) {
    console.log(evt)
  }

  function seek(timestampSeconds: number) {
    const payload: PlayerSeekCommand = {
      type: EventType.PLAYER,
      kind: PlayerCommand.Seek,
      timestampSeconds,
    }
    client.sendJson(payload)
  }

  return {
    state,
    handleIncomingWebSocketEvent,
    seek,
  }
})
