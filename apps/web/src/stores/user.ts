import { userApi } from '@/lib/api'
import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import { jwtDecode } from 'jwt-decode'
import { type UserRole, type JwtPayload } from '@/types/user'
import { client } from '@/lib/SocketClient'

export const useUserStore = defineStore('user', () => {
  function loadAccessToken() {
    const accessToken = localStorage.getItem('access_token')
    if (accessToken) {
      const payload = jwtDecode<JwtPayload>(accessToken)
      console.log(payload)
      user.value = payload
    } else {
      user.value = null
    }
  }

  const user = ref<JwtPayload | null>(null)

  const isLoggedIn = computed(() => !!user.value?.sub)
  const roles = computed(() => user.value?.roles ?? [])
  const isAdmin = computed(() => roles.value.some((el) => el.authority === 'ADMIN'))
  const highestRole = computed<UserRole>(() => {
    if (isAdmin.value) return 'ADMIN'
    return 'USER'
  })
  const username = computed(() => user.value?.username ?? 'anonymous')
  const usernameShort = computed(() => username.value.substring(0, 2).toUpperCase())

  async function signin(username: string, password: string) {
    const res = await userApi.login({ username, password })
    const jwt = res.headers['authorization']
    localStorage.setItem('access_token', 'Bearer ' + jwt)
    loadAccessToken()
    // client.connect()
    return
  }

  async function signout() {
    userApi.logout()
    loadAccessToken()
  }

  function handleIncomingWebSocketEvent(event: unknown) {}

  loadAccessToken()
  return {
    user,
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
