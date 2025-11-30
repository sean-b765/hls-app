<script setup lang="ts">
import { Button } from '@/components/ui/button'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu'
import { formatSeconds } from '@/lib/utils'
import { useRoomStore } from '@/stores/room'
import { Media } from '@hls-app/sdk'
import Hls, { Level } from 'hls.js'
import { Maximize, PauseIcon, PlayIcon, Settings } from 'lucide-vue-next'
import { storeToRefs } from 'pinia'
import { computed, onMounted, ref, useTemplateRef } from 'vue'

const player = useTemplateRef<HTMLVideoElement>('player')
let hls: Hls

const roomStore = useRoomStore()
const { playerState } = storeToRefs(roomStore)
const { media } = defineProps<{ media: Media }>()
const qualities = ref<Level[]>([])
const currentQuality = ref(0)
const duration = ref(0)
const currentTime = ref(0)
const buffered = ref(0)
const hoverDurationX = ref<number | null>(null)

const playedPercent = computed(() => {
  if (!duration.value) return 0
  return (currentTime.value / duration.value) * 100
})

const bufferedPercent = computed(() => {
  if (!duration.value) return 0
  return (buffered.value / duration.value) * 100
})

const hoverPercent = computed(() => {
  if (!hoverDurationX.value) return 0
  return hoverDurationX.value
})

const url = computed(() => {
  if (media) return 'http://localhost:8080/api/playlist/' + media.id
  else return ''
})

function setQuality(level: number) {
  hls.currentLevel = level
}

function getSeekPercent(event: MouseEvent) {
  const el = event.currentTarget as HTMLElement
  const rect = el.getBoundingClientRect()
  const percent = ((event.clientX - rect.left) / rect.width) * 100
  return Math.min(100, Math.max(0, percent))
}

function onSeek(event: MouseEvent) {
  if (!player.value || !duration.value) return
  const percent = getSeekPercent(event)
  player.value.currentTime = (percent / 100) * duration.value
}

function onHover(event: MouseEvent) {
  hoverDurationX.value = getSeekPercent(event)
}

function playPauseToggle() {
  const vid = player.value
  if (vid === null) return
  if (playerState.value.playing) {
    vid.pause()
  } else {
    vid.play()
  }
}

function registerPlayerStateListeners() {
  const vid = player.value
  if (vid === null) return
  vid.onplay = () => {
    playerState.value.playing = true
  }
  vid.onpause = () => {
    playerState.value.playing = false
  }
  vid.ondurationchange = () => {
    duration.value = vid.duration
  }
  vid.ontimeupdate = () => {
    currentTime.value = vid.currentTime
    if (vid.buffered.length > 0) {
      buffered.value = vid.buffered.end(vid.buffered.length - 1)
    }
  }
}

function start() {
  if (player.value == null) return

  if (Hls.isSupported()) {
    hls = new Hls()
    hls.attachMedia(player.value)
    hls.on(Hls.Events.MEDIA_ATTACHED, function () {
      hls.on(Hls.Events.MANIFEST_PARSED, function (event, data) {
        qualities.value = data.levels
        currentQuality.value = data.levels[data.firstLevel].id
      })

      hls.loadSource(url.value)
    })
    hls.on(Hls.Events.ERROR, function (event, data) {
      switch (data.details) {
        case Hls.ErrorDetails.FRAG_LOAD_ERROR:
          console.log('error: FRAG_LOAD_ERROR')
          break
        case Hls.ErrorDetails.ATTACH_MEDIA_ERROR:
          console.log('error: ATTACH_MEDIA_ERROR')
          break
        default:
          console.log('error: ' + data.details, data.error)
          console.log(data)
          break
      }
    })
  } else if (player.value.canPlayType('application/vnd.apple.mpegurl') !== '') {
    player.value.src = url.value
  } else {
    throw new Error('hls not supported')
  }
}

onMounted(() => {
  start()
  registerPlayerStateListeners()
})
</script>

<template>
  <div class="w-full h-fit flex flex-col items-center justify-center relative">
    <span class="loader absolute"></span>
    <video
      ref="player"
      id="video-player"
      :autoplay="false"
      class="w-full bg-muted/20 h-full border-2 shadow-2xl rounded-lg"
    ></video>
    <div class="w-full flex gap-5 absolute bottom-0 p-4 justify-between items-center">
      <div class="controls-left flex">
        <Button
          variant="outline"
          size="icon-sm"
          class="cursor-pointer rounded-full bg-primary border-primary hover:bg-primary/90"
          @click="playPauseToggle"
        >
          <PlayIcon v-if="!playerState.playing" :size="18" class="text-white" />
          <PauseIcon v-else :size="18" class="text-white" />
        </Button>
      </div>
      <div class="track flex gap-4 w-full items-center">
        <div class="flex items-center justify-end">
          <span class="truncate text-xs text-white">
            {{ formatSeconds(currentTime, true) }}
          </span>
        </div>
        <div
          class="flex grow w-full h-3 cursor-pointer items-center group"
          @click="onSeek"
          @mousemove="onHover"
          @mouseleave="() => (hoverDurationX = null)"
        >
          <div
            class="track-duration flex items-center w-full h-1 rounded-xs bg-muted-foreground/20 relative group-hover:bg-muted-foreground/25"
          >
            <div
              class="track-buffered absolute h-full rounded-xs bg-muted-foreground/40"
              :style="{ width: `${bufferedPercent}%` }"
            ></div>
            <div
              class="track-played z-20 absolute h-full rounded-xs bg-primary"
              :style="{ width: `${playedPercent}%` }"
            ></div>
            <div class="track-scrub z-30 absolute h-3 w-3 rounded-full bg-primary"></div>
            <div
              class="track-scrub-ghost z-0 absolute h-full rounded-xs bg-muted-foreground/70"
              :style="{ width: `${hoverPercent}%` }"
            ></div>
          </div>
        </div>
        <div class="flex items-center justify-start">
          <span class="truncate text-xs text-white">
            {{ formatSeconds(duration, true) }}
          </span>
        </div>
      </div>
      <div class="controls-right flex gap-4 items-center">
        <DropdownMenu>
          <DropdownMenuTrigger>
            <span class="cursor-pointer text-sm flex items-center gap-1 rounded-sm px-2 py-1">
              <Settings :size="16" class="text-white" />
              <span class="text-white">
                {{ qualities.find((q) => q.id === currentQuality)?.height }}
              </span>
            </span>
          </DropdownMenuTrigger>
          <DropdownMenuContent class="flex flex-col" side="left">
            <span
              v-for="quality of qualities"
              :key="quality.id"
              class="truncate text-xs cursor-pointer py-1 px-2 hover:bg-accent"
            >
              {{ quality.height }}
            </span>
          </DropdownMenuContent>
        </DropdownMenu>
        <Button size="icon-sm" class="rounded-full cursor-pointer bg-black group hover:bg-black/80">
          <Maximize class="duration-100 group-hover:scale-110 text-white" />
        </Button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.loader {
  width: 24px;
  height: 24px;
  border: 5px solid var(--color-muted);
  border-bottom-color: var(--color-primary);
  border-radius: 50%;
  display: inline-block;
  box-sizing: border-box;
  animation: rotation 1s ease-in-out infinite;
}

@keyframes rotation {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>
