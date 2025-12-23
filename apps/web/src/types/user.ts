export type UserRole = 'USER' | 'ADMIN'
export type HasAuthenticatedCallback = (payload: JwtPayload | null) => void
export type JwtPayload = {
  aud: string[]
  iss: string
  /**
   * Expiry, seconds since unix epoch
   */
  exp: number
  /**
   * Time jwt was issued at, seconds since unix epoch
   */
  iat: number
  /**
   * User Id
   */
  sub: string
  username: string
  roles: Array<{ authority: UserRole }>
}
