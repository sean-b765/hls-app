import { userApi } from '@/lib/api'
import { defineStore } from 'pinia'
import { computed, ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const user = ref<{ userId: string }>({
    userId: '',
  })

  const isLoggedIn = computed(() => !!user.value.userId)
  async function signin(username: string, password: string) {
    const req = await userApi.login({ username, password })
    const res = await req()
    const jwt = res.headers['authorization']
    localStorage.setItem('access_token', 'Bearer ' + jwt)
    return
  }

  function handleIncomingWebSocketEvent(event: unknown) {}

  return {
    user,
    isLoggedIn,
    signin,
    handleIncomingWebSocketEvent,
  }
})
