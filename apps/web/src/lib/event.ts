import type { GlobalEvents } from '@/types/events'
import mitt from 'mitt'
export const emitter = mitt<GlobalEvents>()
