import { defineStore } from 'pinia'
import { ref } from 'vue'
import { mediaApi } from '@/lib/api.js'
import type { Media } from '@hls-app/sdk'
import type { Progress, ScanProgress } from '@/types/media'

export const useMediaStore = defineStore('media', () => {
  const media = ref<Media[]>([])
  const scanProgress = ref<ScanProgress>({})

  async function getMedia(mediaId: string) {
    const req = await mediaApi.getById(mediaId)
    const res = await req()
    if (res.status !== 200) return

    const found = res.data

    const index = media.value.findIndex((el) => el.id === found.id)
    if (index === -1) {
      media.value.push(found)
    } else {
      media.value.splice(index, 1, found)
    }
  }

  async function progressUpdate(mediaId: string, progress: Progress) {
    scanProgress.value[mediaId] = progress
    console.log('PROGRESS UPDATE', progress)
    if (progress !== 'READY') return
    await getMedia(mediaId)
  }

  function handleScanProgressEvent(update: ScanProgress) {
    for (const mediaId of Object.keys(update)) {
      const oldStatus = scanProgress.value[mediaId]
      if (oldStatus === 'READY') continue

      const newStatus = update[mediaId]
      if (!newStatus || newStatus === oldStatus) continue

      progressUpdate(mediaId, newStatus)
    }
  }

  async function getAll() {
    const request = await mediaApi.getAll()
    const response = await request()
    media.value = response.data
  }

  async function startScanProgress() {
    const events = new EventSource('http://localhost:8080/api/media/scan-progress')
    events.addEventListener('progress', (e: MessageEvent) => {
      const data: ScanProgress = JSON.parse(e.data)
      console.log('INCOMING', data)
      handleScanProgressEvent(data)
    })
  }

  return { media, getAll, startScanProgress, scanProgress }
})
