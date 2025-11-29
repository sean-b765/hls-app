import type { ClassValue } from 'clsx'
import { clsx } from 'clsx'
import { twMerge } from 'tailwind-merge'
import moment from 'moment'

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export function formatSeconds(seconds: number | undefined): string {
  if (seconds === undefined) return '0h'
  const duration = moment.duration(seconds, 'seconds')

  const h = duration.hours()
  const m = duration.minutes()

  const parts: string[] = []
  if (h > 0) parts.push(`${h}h`)
  if (m > 0) parts.push(`${m}m`)

  return parts.join(' ')
}
