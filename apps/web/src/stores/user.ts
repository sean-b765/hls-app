import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const user = ref<{ userId: string }>({
    userId: '',
  })

  function handleIncomingWebSocketEvent(event: unknown) {}

  return {
    user,
    handleIncomingWebSocketEvent,
  }
})
