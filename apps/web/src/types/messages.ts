import type { PlayerStateType, Room } from './room'

export enum EventType {
  ROOM = 'ROOM',
  PLAYER = 'PLAYER',
  CHAT = 'CHAT',
  ERROR = 'ERROR',
  INFO = 'INFO',
}

export type BaseMessage = {
  type: EventType
  kind: string
}

export type WsMessage = BaseMessage & {
  timestamp?: string
  userId?: string
}

export type RoomCommand = BaseMessage & {
  type: EventType.ROOM
}
export type RoomEvent = WsMessage & {
  type: EventType.ROOM
}

export type PlayerCommand = BaseMessage & {
  type: EventType.PLAYER
}
export type PlayerEvent = WsMessage & {
  type: EventType.PLAYER
}

export type ChatCommand = BaseMessage & {
  type: EventType.CHAT
}
export type ChatEvent = WsMessage & {
  type: EventType.CHAT
}

export type RoomCreateCommand = RoomCommand & {
  kind: typeof RoomCommand.Create
  name: string
}
export type RoomJoinCommand = RoomCommand & {
  kind: typeof RoomCommand.Join
  code: string
}
export type RoomLeaveCommand = RoomCommand & {
  kind: typeof RoomCommand.Leave
}

export type RoomCreatedEvent = RoomEvent & {
  kind: typeof RoomEvent.Created
  room: Room
}
export type RoomJoinedEvent = RoomEvent & {
  kind: typeof RoomEvent.Joined
  room: Room
}
export type RoomLeftEvent = RoomEvent & {
  kind: typeof RoomEvent.Left
}
export type AnyRoomEvent = RoomCreatedEvent | RoomJoinedEvent | RoomLeftEvent
export type RoomEventByKind = {
  [RoomEvent.Created]: RoomCreatedEvent
  [RoomEvent.Joined]: RoomJoinedEvent
  [RoomEvent.Left]: RoomLeftEvent
}
export type RoomEventHandlers = {
  [K in keyof RoomEventByKind]: (evt: RoomEventByKind[K]) => void
}

export type PlayerChooseMediaCommand = PlayerCommand & {
  kind: typeof PlayerCommand.ChooseMedia
  mediaId: string
}
export type PlayerSeekCommand = PlayerCommand & {
  kind: typeof PlayerCommand.Seek
  timestampSeconds: number
}
export type PlayerStateChangeCommand = PlayerCommand & {
  kind: typeof PlayerCommand.StateChange
  state: PlayerStateType
}

export type PlayerChoseMediaEvent = PlayerEvent & {
  kind: typeof PlayerEvent.ChoseMedia
  mediaId: string
}
export type PlayerSeekedEvent = PlayerEvent & {
  kind: typeof PlayerEvent.Seeked
  timestampSeconds: number
}
export type PlayerStateChangedEvent = PlayerEvent & {
  kind: typeof PlayerEvent.StateChanged
  state: PlayerStateType
}

export const RoomCommand = {
  Create: 'RoomCreateCommand',
  Join: 'RoomJoinCommand',
  Leave: 'RoomLeaveCommand',
} as const
export const RoomEvent = {
  Created: 'RoomCreatedEvent',
  Joined: 'RoomJoinedEvent',
  Left: 'RoomLeftEvent',
} as const
export type RoomEventTypes = (typeof RoomEvent)[keyof typeof RoomEvent]

export const PlayerCommand = {
  ChooseMedia: 'PlayerChooseMediaCommand',
  StateChange: 'PlayerStateChangeCommand',
  Seek: 'PlayerSeekCommand',
} as const
export const PlayerEvent = {
  ChoseMedia: 'PlayerChoseMediaEvent',
  StateChanged: 'PlayerStateChangedEvent',
  Seeked: 'PlayerSeekedEvent',
} as const
