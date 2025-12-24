export type Library = {
  id: string
  name: string
  order: number
  type: LibraryType
}
export type LibraryType = 'TV' | 'MOVIES' | 'MUSIC' | 'ANIME' | 'OTHER'
