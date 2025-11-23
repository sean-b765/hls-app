export {}

declare global {
  interface Window {
    _socketClient?: SocketClient
  }
}
