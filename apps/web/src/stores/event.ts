import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import type { AnyRoomEvent, BaseMessage, ResourceUpdatedEvent, WsMessage } from '@/types/messages'
import { EventType } from '@/types/messages'
import { useRoomStore } from './room'
import { useMediaStore } from './media'
import { useLibraryStore } from './library'

export const useEventStore = defineStore('events', () => {
  const { handleIncomingWebSocketEvent } = useRoomStore()
  const { getById: getMedia } = useMediaStore()
  const { getById: getLibrary } = useLibraryStore()

  const events = ref({
    sent: [] as BaseMessage[],
    received: [] as WsMessage[],
  })

  const sent = computed(() => events.value.sent)
  const received = computed(() => events.value.received)

  function fetchResource(event: ResourceUpdatedEvent) {
    switch (event.uri) {
      case '/api/media':
        getMedia(event.id)
        break
      case '/api/library':
        getLibrary(event.id)
        break
    }
  }

  function handleEvent(event: BaseMessage) {
    switch (event.type) {
      case EventType.RESOURCE_UPDATED:
        fetchResource(event as ResourceUpdatedEvent)
        break
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
