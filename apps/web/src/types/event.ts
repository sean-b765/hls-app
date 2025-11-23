import type { Room } from './room'

export type BaseEvent = {
  type: EventType
  timestamp?: string
}

export type RoomEvent = BaseEvent & {
  eventType: RoomEventType
}

export type PlayerEvent = BaseEvent & {
  eventType: PlayerEventType
}

export type ChatEvent = BaseEvent & {
  chat: Chat
}

export type RoomCreateEvent = RoomEvent & {
  roomName: string
}

export type JoinRoomEvent = RoomEvent & {
  roomCode: string
}

export type RoomCreatedEvent = RoomEvent & {
  room: Room
}

export type ChooseTrackEvent = PlayerEvent & {
  trackId: string
}

export type EventType = 'ROOM' | 'PLAYER' | 'CHAT'
export type RoomEventType = 'ROOM_CREATE' | 'ROOM_JOIN' | 'ROOM_LEAVE'
export type PlayerEventType = 'CHOOSE_TRACK' | 'PLAYER_STATE'
export type Chat = {
  message: string
  userId: string
}
