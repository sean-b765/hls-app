<script setup lang="ts">
import { Button } from '@/components/ui/button'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu'
import { Slider } from '@/components/ui/slider'
import { authApi } from '@/lib/api'
import { formatSeconds } from '@/lib/utils'
import { usePlayerStore } from '@/stores/player'
import { Media } from '@hls-app/sdk'
import Hls, { Level } from 'hls.js'
import {
  Maximize,
  PauseIcon,
  PlayIcon,
  SlidersVertical,
  Volume,
  Volume1,
  Volume2,
  VolumeX,
} from 'lucide-vue-next'
import { storeToRefs } from 'pinia'
import { computed, onBeforeUnmount, onMounted, ref, useTemplateRef, watch } from 'vue'

const player = useTemplateRef<HTMLVideoElement>('player')
let hls: Hls

const playerStore = usePlayerStore()
const { state } = storeToRefs(playerStore)
const { media } = defineProps<{ media: Media }>()
const qualities = ref<Level[]>([])
const volume = ref([1])
watch(volume, ([vol]) => {
  const vid = player.value
  if (vid == null) return
  vid.volume = vol
})

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
  if (media) return import.meta.env.VITE_BASE_URL + '/api/playlist/' + media.id
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
  const seekTime = (percent / 100) * duration.value
  playerStore.seek(seekTime)
}

function onHover(event: MouseEvent) {
  hoverDurationX.value = getSeekPercent(event)
}

function playPauseToggle() {
  const vid = player.value
  if (vid === null) return
  if (state.value.playing) {
    vid.pause()
  } else {
    vid.play()
  }
}

function fullscreen() {
  const vid = player.value
  if (vid == null) return
  const videoContainer = vid.parentElement!

  if (document.fullscreenElement === videoContainer) {
    document.exitFullscreen()
  } else {
    videoContainer.requestFullscreen()
  }
}

function registerPlayerStateListeners() {
  const vid = player.value
  if (vid === null) return
  vid.onplay = () => {
    state.value.playing = true
  }
  vid.onpause = () => {
    state.value.playing = false
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
    hls = new Hls({
      fragLoadingMaxRetry: 5,
      fragLoadingRetryDelay: 1000,
      fragLoadingTimeOut: 20_000,
      levelLoadingMaxRetry: 3,
      manifestLoadingMaxRetry: 3,
      maxBufferHole: 0.5,
      xhrSetup(xhr) {
        xhr.withCredentials = true
        xhr.setRequestHeader('Authorization', localStorage.getItem('access_token') ?? '')
        const hlsToken = sessionStorage.getItem('hls_token') ?? ''
        if (hlsToken) xhr.setRequestHeader('X-Hls-Token', hlsToken)
      },
    })

    hls.on(Hls.Events.MANIFEST_LOADED, (event, data) => {
      const details = data.networkDetails as XMLHttpRequest
      const hlsToken = details.getResponseHeader('X-Hls-Token')
      if (!hlsToken) return
      sessionStorage.setItem('hls_token', hlsToken)
    })

    hls.on(Hls.Events.MEDIA_ATTACHED, function () {
      hls.on(Hls.Events.MANIFEST_PARSED, function (event, data) {
        hls.currentLevel = 0
        hls.nextLevel = 0
        hls.loadLevel = 0
        qualities.value = data.levels
        currentQuality.value = hls.currentLevel
      })

      hls.loadSource(url.value)
    })
    hls.on(Hls.Events.ERROR, async function (event, data) {
      if (data.response?.code === 403) {
        // Access was denied. Try to generate a new token
        console.log('ACCESS DENIED!!!!! GENERATE')
        await authApi.generateHlsToken(media.id)
      }
    })
  } else if (player.value.canPlayType('application/vnd.apple.mpegurl') !== '') {
    player.value.src = url.value
  } else {
    throw new Error('hls not supported')
  }
  hls.attachMedia(player.value)
}

onMounted(() => {
  start()
  registerPlayerStateListeners()
})

onBeforeUnmount(() => {
  hls.stopLoad()
  hls.destroy()
  hls = null
})
</script>

<template>
  <div class="video-container w-full h-fit flex flex-col items-center justify-center relative">
    <span v-if="duration <= buffered" class="loader absolute"></span>
    <video
      ref="player"
      id="video-player"
      :autoplay="false"
      class="w-full h-full rounded-lg bg-black"
      @click="playPauseToggle"
      @dblclick="fullscreen"
    ></video>
    <div class="w-full bottom-0 p-2 absolute">
      <div
        class="controls w-full flex gap-3 p-1 justify-between items-center pa-2 bg-card/70 rounded-md"
      >
        <div class="controls-left flex">
          <Button variant="ghost" size="icon-sm" class="cursor-pointer" @click="playPauseToggle">
            <PlayIcon v-if="!state.playing" :size="18" fill="currentColor" />
            <PauseIcon v-else :size="18" fill="currentColor" />
          </Button>
        </div>
        <div class="track flex gap-4 w-full items-center">
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
              <div
                class="track-scrub z-30 absolute h-2 w-2 rounded-full bg-primary/80 opacity-0 transition-all group-hover:opacity-100"
                :style="{ left: `${playedPercent}%` }"
              ></div>
              <div
                class="track-scrub-ghost z-0 absolute h-full rounded-xs bg-muted-foreground/70 opacity-0 transition-opacity group-hover:opacity-100"
                :style="{ width: `${hoverPercent}%` }"
              ></div>
            </div>
          </div>
          <div class="flex items-center justify-end">
            <span class="truncate text-xs">
              {{ formatSeconds(currentTime, true) }}
            </span>
            <span class="text-xs mx-1 opacity-40">/</span>
            <span class="truncate text-xs opacity-70">
              {{ formatSeconds(duration, true) }}
            </span>
          </div>
        </div>
        <div class="controls-right flex gap-2 items-center">
          <DropdownMenu>
            <DropdownMenuTrigger>
              <Button size="icon-sm" variant="ghost">
                <Volume2 v-if="volume[0] > 0.66" :size="16" />
                <Volume1 v-else-if="volume[0] > 0.33" :size="16" />
                <Volume v-else-if="volume[0] > 0" :size="16" />
                <VolumeX v-else />
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent>
              <Slider
                v-model:model-value="volume"
                :min="0"
                :default-value="volume"
                :step="0.05"
                :max="1"
              />
            </DropdownMenuContent>
          </DropdownMenu>
          <DropdownMenu>
            <DropdownMenuTrigger>
              <Button size="icon-sm" variant="ghost">
                <SlidersVertical :size="16" />
              </Button>
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
          <Button size="icon-sm" variant="ghost" class="cursor-pointer" @click="fullscreen">
            <Maximize />
          </Button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.video-container {
  &:hover {
    .controls {
      opacity: 1;
    }
  }
  .controls {
    opacity: 0;
    transition: opacity 0.4s cubic-bezier(0.19, 1, 0.22, 1);
  }
}
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
