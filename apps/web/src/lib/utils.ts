import type { ClassValue } from 'clsx'
import { clsx } from 'clsx'
import { twMerge } from 'tailwind-merge'
import moment from 'moment'
import { padStart } from 'lodash'

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
