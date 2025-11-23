import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import type { BaseEvent } from '@/types/event'

export const useEventStore = defineStore('events', () => {
  const events = ref({
    sent: [] as BaseEvent[],
    received: [] as BaseEvent[],
  })

  const sent = computed(() => events.value.sent)
  const received = computed(() => events.value.received)

  function addSent(event: BaseEvent) {
    events.value.sent.push(event)
  }

  function addReceived(event: BaseEvent) {
    events.value.received.push(event)
  }

  function clearSent() {
    events.value.sent = []
  }
  function clearReceived() {
    events.value.received = []
  }

  return { sent, received, addSent, addReceived, clearReceived, clearSent }
})
