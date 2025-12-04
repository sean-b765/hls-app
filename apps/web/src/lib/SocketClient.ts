import { ref } from 'vue'
import { type BaseEvent } from '@/types/event'
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
    this.client = new WebSocket(this.url)
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
      }
    } catch (e) {
      console.error('[WS ERROR] Failed to parse message', message.data, e)
    }
  }

  private processString(data: string) {
    const event = JSON.parse(data) as BaseEvent
    const { addReceived } = useEventStore()

    addReceived(event)
    console.log('[WS EVENT]', event)
  }

  private processBlob(data: Blob) {
    console.log('we got some shit', data.size)
  }

  public sendJson(data: BaseEvent) {
    // Maybe push messages to a DLQ in the event the server is unreachable...
    if (!this.client || this.client.readyState !== WebSocket.OPEN) return
    this.client.send(JSON.stringify(data))
    const { addSent } = useEventStore()
    addSent(data)
  }

  get state() {
    return this._state.value
  }
}

export const client = new SocketClient(import.meta.env.VITE_BASE_URL.replace('http', 'ws') + '/ws')

window.addEventListener('beforeunload', () => client.disconnect())
if (import.meta.hot) {
  import.meta.hot.on('vite:beforeUpdate', () => client.disconnect())
}
