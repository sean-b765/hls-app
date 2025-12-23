import { AuthAPI } from '@/lib/api'
import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import { type UserRole, type JwtPayload } from '@/types/user'
import { client } from '@/lib/SocketClient'
import { emitter } from '@/lib/event'

export const useUserStore = defineStore('user', () => {
  const user = ref<JwtPayload | null>()
  // Register the auth listener before constructing the auth api
  emitter.on('auth', (jwt) => {
    if (!jwt) {
      user.value = null
      client.disconnect()
      return
    }
    user.value = jwt
    client.connect()
  })

  const isReady = computed(() => user.value !== undefined)
  const isLoggedIn = computed(() => !!user.value?.sub)
  const roles = computed(() => user.value?.roles ?? [])
  const isAdmin = computed(() => roles.value.some((el) => el.authority === 'ADMIN'))
  const highestRole = computed<UserRole>(() => {
    if (isAdmin.value) return 'ADMIN'
    return 'USER'
  })
  const username = computed(() => user.value?.username ?? 'anonymous')
  const usernameShort = computed(() => username.value.substring(0, 2).toUpperCase())

  const authApi = new AuthAPI()
  async function signin(username: string, password: string) {
    await authApi.login({ username, password })
  }

  async function signout() {
    authApi.logout()
  }

  function handleIncomingWebSocketEvent(event: unknown) {}

  return {
    user,
    isReady,
    isLoggedIn,
    isAdmin,
    username,
    usernameShort,
    highestRole,
    signin,
    signout,
    handleIncomingWebSocketEvent,
  }
})
