export type UserRole = 'USER' | 'ADMIN'

export type JwtPayload = {
  aud: string[]
  iss: string
  exp: number
  iat: number
  /**
   * User Id
   */
  sub: string
  username: string
  roles: Array<{ authority: UserRole }>
}
