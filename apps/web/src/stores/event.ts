import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import type { AnyRoomEvent, BaseMessage, WsMessage } from '@/types/messages'
import { EventType } from '@/types/messages'
import { useRoomStore } from './room'

export const useEventStore = defineStore('events', () => {
  const { handleIncomingWebSocketEvent } = useRoomStore()

  const events = ref({
    sent: [] as BaseMessage[],
    received: [] as WsMessage[],
  })

  const sent = computed(() => events.value.sent)
  const received = computed(() => events.value.received)

  function handleEvent(event: BaseMessage) {
    switch (event.type) {
      case EventType.ROOM:
        handleIncomingWebSocketEvent(event as AnyRoomEvent)
        break
    }
  }

  function outgoing(event: BaseMessage) {
    events.value.sent.push(event)
  }

  function incoming(event: WsMessage) {
    events.value.received.push(event)
    handleEvent(event)
  }

  function clearSent() {
    events.value.sent = []
  }
  function clearReceived() {
    events.value.received = []
  }

  return { sent, received, outgoing, incoming, clearReceived, clearSent }
})
