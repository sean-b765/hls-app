export type Room = {
  code: string
  name: string
}

export type PlayerState = {
  playing: boolean
  loaded: boolean
  buffering: boolean
}

export enum PlayerStateType {
  PLAYING = 'PLAYING',
  BUFFERING = 'BUFFERING',
  PAUSED = 'PAUSED',
}
