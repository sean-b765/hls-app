import type { JwtPayload } from './user'

export type GlobalEvents = {
  auth: JwtPayload | null
}
