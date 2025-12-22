import { ref } from 'vue'
import { type BaseMessage } from '@/types/messages'
import { useEventStore } from '@/stores/event'

export class SocketClient {
  private url: string
  private client: WebSocket | null = null

  private _state = ref({
    connected: false,
  })

  constructor(url: string) {
    this.url = url
  }

  public connect() {
    const token = encodeURIComponent(localStorage.getItem('access_token') ?? '')
    if (token) {
      this.client = new WebSocket(this.url, ['Authorization', token])
    } else {
      this.client = new WebSocket(this.url)
    }
    this.client.onopen = () => this.open()
    this.client.onclose = () => this.close()
    this.client.onerror = () => this.onerror()
    this.client.onmessage = (e) => this.onmessage(e)
  }

  public disconnect() {
    if (!this.client) return
    this.close()
    this.client.close()
    this.client.onopen = null
    this.client.onclose = null
    this.client.onerror = null
    this.client.onmessage = null
  }

  private open() {
    this._state.value.connected = true
  }

  private close() {
    this._state.value.connected = false
  }

  private onerror() {
    this._state.value.connected = false
  }

  private onmessage(message: MessageEvent) {
    try {
      switch (typeof message.data) {
        case 'string':
          this.processString(message.data)
          break
        case 'object':
          if (message.data instanceof Blob) this.processBlob(message.data)
          break
      }
    } catch (e) {
      console.error('[WS ERROR] Failed to parse message', message.data, e)
    }
  }

  private processString(data: string) {
    const event = JSON.parse(data) as BaseMessage
    const { incoming } = useEventStore()

    incoming(event)
    console.log('%c[INCOMING WS EVENT]\n', 'background-color: green; color: black;', event)
  }

  private processBlob(data: Blob) {
    console.log('we got some shit', data.size)
  }

  public sendJson(data: BaseMessage) {
    // Maybe push messages to a DLQ in the event the server is unreachable...
    if (!this.client || !this.isConnected) return
    this.client.send(JSON.stringify(data))
    const { outgoing } = useEventStore()
    outgoing(data)
    console.log('%c[OUTGOING WS EVENT]\n', 'background-color: blue; color: white;', data)
  }

  get state() {
    return this._state.value
  }
  get isConnected() {
    if (!this.client) return false
    return this.client?.readyState === WebSocket.OPEN
  }
}

export const client = new SocketClient(import.meta.env.VITE_BASE_URL.replace('http', 'ws') + '/ws')

window.addEventListener('beforeunload', () => client.disconnect())
if (import.meta.hot) {
  import.meta.hot.on('vite:beforeUpdate', () => client.disconnect())
}
