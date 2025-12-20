import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import { client } from '@/lib/SocketClient'
import {
  type RoomJoinCommand,
  type RoomCreateCommand,
  type RoomJoinedEvent,
  type PlayerSeekCommand,
  type RoomEventHandlers,
  type RoomCreatedEvent,
  type AnyRoomEvent,
  type RoomLeftEvent,
  EventType,
  PlayerCommand,
  RoomCommand,
  RoomEvent,
} from '@/types/messages'
import type { Room } from '@/types/room'

export const useRoomStore = defineStore('room', () => {
  const handlers: RoomEventHandlers = {
    [RoomEvent.Created]: roomCreated,
    [RoomEvent.Joined]: roomJoined,
    [RoomEvent.Left]: roomLeft,
  }

  const room = ref<Partial<Room>>({
    code: undefined,
    name: undefined,
  })

  const isConnected = computed(() => !!room.value.code)

  function handleIncomingWebSocketEvent(event: AnyRoomEvent) {
    const handlerForEvent = handlers[event.kind]
    if (!handlerForEvent) return

    handlerForEvent(event as never)
  }

  function roomCreated(evt: RoomCreatedEvent) {
    console.log(evt)
  }

  function roomJoined(evt: RoomJoinedEvent) {
    console.log(evt)
    room.value = evt.room
  }

  function roomLeft(evt: RoomLeftEvent) {
    console.log(evt)
  }

  function createRoom(name: string) {
    const payload: RoomCreateCommand = {
      type: EventType.ROOM,
      kind: RoomCommand.Create,
      name,
    }
    client.sendJson(payload)
  }

  function joinRoom(code: string) {
    const payload: RoomJoinCommand = { type: EventType.ROOM, kind: RoomCommand.Join, code }
    client.sendJson(payload)
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
    room,
    isConnected,
    handleIncomingWebSocketEvent,
    createRoom,
    joinRoom,
    seek,
  }
})
