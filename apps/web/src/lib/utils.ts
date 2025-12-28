import type { ClassValue } from 'clsx'
import { clsx } from 'clsx'
import { twMerge } from 'tailwind-merge'
import moment from 'moment'
import { padStart } from 'lodash'
import { Clapperboard, JapaneseYen, Music, Shapes, TvMinimal } from 'lucide-vue-next'
import type { Library } from '@hls-app/sdk'

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export function formatDuration(seconds: number | undefined): string {
  if (seconds === undefined) return '0h'
  const duration = moment.duration(seconds, 'seconds')

  const h = duration.hours()
  const m = duration.minutes()

  const parts: string[] = []
  if (h > 0) parts.push(`${h}h`)
  if (m > 0) parts.push(`${m}m`)

  return parts.join(' ')
}

export function formatSeconds(seconds: number | undefined, pad = false): string {
  if (seconds === undefined) return '0'
  const duration = moment.duration(seconds, 'seconds')

  const h = duration.hours()
  const m = duration.minutes()
  const s = duration.seconds()

  const parts: string[] = []
  if (h > 0 || pad) parts.push(`${h}`)
  if (m > 0 || pad) parts.push(`${m}`)
  if (s > 0 || pad) parts.push(`${s}`)

  return parts.map((p) => (!pad ? p : padStart(p, 2, '0'))).join(':')
}

export function getImage(path: string) {
  return `https://image.tmdb.org/t/p/w1920${path}`
}

export function generateUID() {
  let firstPart: string | number = (Math.random() * 46656) | 0
  let secondPart: string | number = (Math.random() * 46656) | 0
  firstPart = ('000' + firstPart.toString(36)).slice(-3)
  secondPart = ('000' + secondPart.toString(36)).slice(-3)
  return firstPart + secondPart
}

export function getLibraryIcon(libraryOrIcon?: Library | string) {
  if (!libraryOrIcon) return
  const type = typeof libraryOrIcon === 'string' ? libraryOrIcon : libraryOrIcon.type
  switch (type) {
    case 'MOVIES':
      return Clapperboard
    case 'TV':
      return TvMinimal
    case 'MUSIC':
      return Music
    case 'ANIME':
      return JapaneseYen
    case 'OTHER':
      return Shapes
    default:
      return Clapperboard
  }
}
