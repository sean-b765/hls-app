<script setup lang="ts">
import { Button } from '@/components/ui/button'
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover'
import { Slider } from '@/components/ui/slider'
import { authApi } from '@/lib/api'
import { formatSeconds, getImage, supportsPiP } from '@/lib/utils'
import { usePlayerStore } from '@/stores/player'
import { Media } from '@hls-app/sdk'
import Hls, { Level } from 'hls.js'
import {
  Maximize,
  MoveLeft,
  PauseIcon,
  PictureInPicture2,
  PlayIcon,
  SlidersVertical,
  Volume,
  Volume1,
  Volume2,
  VolumeX,
} from 'lucide-vue-next'
import { storeToRefs } from 'pinia'
import { computed, onBeforeUnmount, onMounted, ref, useTemplateRef, watch } from 'vue'
import { useRouter } from 'vue-router'

const player = useTemplateRef<HTMLVideoElement>('player')
let hls: Hls

const router = useRouter()
const playerStore = usePlayerStore()
const { state } = storeToRefs(playerStore)
const { media } = defineProps<{ media: Media }>()

const loaded = ref(false)
const qualities = ref<Level[]>([])
const volume = ref([parseFloat(localStorage.getItem('volume') ?? '1')])
const currentQuality = ref(0)
const duration = ref(0)
const currentTime = ref(parseFloat(localStorage.getItem(`currentTime_${media.id}`) ?? '0'))
const buffered = ref(0)
const hoverDurationX = ref<number | null>(null)

watch(volume, ([vol]) => {
  const vid = player.value
  if (vid == null) return
  vid.volume = vol
  localStorage.setItem('volume', vol.toString())
})

const loading = computed(() => {
  const hasBufferedData = buffered.value !== 0
  return (
    !loaded.value ||
    (hasBufferedData && currentTime.value >= buffered.value) ||
    state.value.buffering
  )
})

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

function pictureInPicture() {
  if (!supportsPiP()) return

  const vid = player.value
  if (vid == null) return

  if ('webkitEnterPictureInPicture' in vid) {
    // @ts-expect-error safari
    if (vid.webkitPresentationMode === 'picture-in-picture') {
      // @ts-expect-error safari
      vid.webkitExitPictureInPicture()
    } else {
      // @ts-expect-error safari
      vid.webkitEnterPictureInPicture()
    }
    return
  }

  if (document.pictureInPictureElement === vid) {
    document.exitPictureInPicture()
  } else {
    vid.requestPictureInPicture()
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
      console.log('loaded')
    })

    hls.on(Hls.Events.MEDIA_ATTACHED, function () {
      hls.on(Hls.Events.MANIFEST_PARSED, function (event, data) {
        hls.currentLevel = 0
        hls.nextLevel = 0
        hls.loadLevel = 0
        qualities.value = data.levels
        currentQuality.value = hls.currentLevel
        // Set currentTime
        if (player.value) player.value.currentTime = currentTime.value
      })

      hls.loadSource(url.value)
    })
    hls.on(Hls.Events.LEVEL_LOADED, () => {
      loaded.value = true
    })
    hls.on(Hls.Events.ERROR, async function (event, data) {
      if (data.response?.code === 403) {
        // Access was denied. Try to generate a new token
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
  localStorage.setItem(`currentTime_${media.id}`, currentTime.value.toString())
})
</script>

<template>
  <div
    class="video-container w-full h-svh flex flex-col items-start justify-start relative group bg-black"
    :class="{ paused: !state.playing }"
  >
    <video
      ref="player"
      id="video-player"
      :autoplay="false"
      class="w-full h-full relative bg-black"
      :disable-picture-in-picture="supportsPiP()"
      @click.capture="playPauseToggle"
      @dblclick="fullscreen"
    ></video>
    <!-- Back button -->
    <Button
      size="icon-lg"
      variant="ghost"
      class="show-hover-paused absolute cursor-pointer rounded-full z-10 bg-transparent hover:bg-transparent"
      @click="() => router.back()"
    >
      <MoveLeft color="white" />
    </Button>

    <!-- Fullscreen overview -->
    <div
      class="show-hover-paused w-full h-auto flex pointer-events-none absolute overview gap-7 pt-10 pl-4 sm:pl-14 overflow-hidden opacity-0 group-hover:opacity-100"
    >
      <img
        v-if="media.info?.thumbnail"
        :src="getImage(media.info?.thumbnail)"
        width="90px"
        class="show-hover-paused rounded-xs hidden sm:block"
      />
      <div class="flex flex-col gap-3 mt-2 pr-4">
        <!-- Overview -->
        <span class="w-full text-wrap font-bold text-sm text-white">
          {{ media?.info?.name }}
        </span>
        <span class="w-full text-wrap text-xs text-white text-shadow-xs text-shadow-black max-w-xl">
          {{ media?.info?.description }}
        </span>
      </div>
    </div>

    <!-- Loading/buffering indicator -->
    <span
      v-if="loading"
      class="loader pointer-events-none absolute z-10 left-1/2 top-1/2 -translate-1/2"
    ></span>

    <!-- Play/Pause/jump forwards-backwards -->
    <div
      v-if="loaded"
      class="play-pause show-hover-paused absolute flex left-1/2 top-1/2 -translate-1/2"
    >
      <Button
        variant="ghost"
        style="height: 48px; width: 48px"
        class="cursor-pointer rounded-full flex justify-center hover:bg-transparent"
        @click="playPauseToggle"
      >
        <PlayIcon
          v-if="!state.playing"
          style="width: 40px; height: 40px"
          color="white"
          fill="white"
          stroke-opacity="0"
        />
        <PauseIcon
          v-else
          style="width: 40px; height: 40px"
          color="white"
          fill="white"
          stroke-opacity="0"
        />
      </Button>
    </div>

    <!-- Controls -->
    <div
      class="controls show-hover-paused absolute bottom-0 w-full flex gap-3 justify-between items-center p-2 pl-4 pr-2 rounded-md"
    >
      <div
        class="track flex gap-4 w-full items-center transition-opacity duration-200"
        :class="{ 'pointer-events-none': !loaded, 'opacity-30': !loaded }"
      >
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
              class="track-scrub z-30 absolute h-2 w-2 rounded-full bg-primary/80 transition-all"
              :style="{ left: `${playedPercent}%` }"
            ></div>
            <div
              class="track-scrub-ghost z-0 absolute h-full rounded-xs bg-muted-foreground/70 transition-opacity"
              :style="{ width: `${hoverPercent}%` }"
            ></div>
          </div>
        </div>
        <div class="flex items-center justify-end text-white">
          <span class="truncate text-xs font-mono">
            {{ formatSeconds(currentTime, true) }}
          </span>
          <span class="text-xs mx-1 opacity-40">/</span>
          <span class="truncate text-xs opacity-70 font-mono">
            {{ formatSeconds(duration, true) }}
          </span>
        </div>
      </div>
      <div
        class="controls-right flex gap-2 items-center text-white transition-opacity duration-200"
        :class="{ 'pointer-events-none': !loaded, 'opacity-30': !loaded }"
      >
        <!-- Volume -->
        <Popover>
          <PopoverTrigger>
            <Button size="icon-sm" variant="ghost">
              <Volume2 v-if="volume[0] > 0.66" :size="16" />
              <Volume1 v-else-if="volume[0] > 0.33" :size="16" />
              <Volume v-else-if="volume[0] > 0" :size="16" />
              <VolumeX v-else />
            </Button>
          </PopoverTrigger>
          <PopoverContent
            side="top"
            :side-offset="10"
            class="bg-black border-2 border-muted/10 mr-2"
          >
            <Slider
              v-model:model-value="volume"
              :min="0"
              :default-value="volume"
              :step="0.05"
              :max="1"
            />
          </PopoverContent>
        </Popover>
        <!-- Quality -->
        <Popover>
          <PopoverTrigger>
            <Button size="icon-sm" variant="ghost">
              <SlidersVertical :size="16" />
            </Button>
          </PopoverTrigger>
          <PopoverContent
            class="w-30 p-2 flex flex-col bg-black border-2 gap-1 border-muted/10 mr-2"
            side="top"
            :side-offset="10"
          >
            <span
              v-for="quality of qualities"
              :key="quality.id"
              class="truncate text-sm p-1 px-2 rounded-xs text-white cursor-pointer hover:bg-muted/20"
            >
              {{ quality.height }}p
            </span>
          </PopoverContent>
        </Popover>
        <Button size="icon-sm" variant="ghost" class="cursor-pointer" @click="fullscreen">
          <Maximize />
        </Button>
        <Button
          v-if="supportsPiP()"
          size="icon-sm"
          variant="ghost"
          class="cursor-pointer"
          @click="pictureInPicture"
        >
          <PictureInPicture2 />
        </Button>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.video-container {
  .show-hover-paused,
  .show-paused {
    opacity: 0;
    transition: opacity 1s cubic-bezier(0.19, 1, 0.22, 1);
  }
  &:hover,
  &.paused {
    .show-hover-paused {
      opacity: 1;
    }
  }
  &.paused {
    .show-paused {
      opacity: 1;
    }
  }
}
.loader {
  width: 60px;
  height: 60px;
  border: 4px solid #ffffff1b;
  border-bottom-color: var(--color-primary);
  border-radius: 50%;
  display: inline-block;
  box-sizing: border-box;
  animation:
    rotation 1s ease-in-out infinite,
    rotate 2s ease-in-out infinite;
}

@keyframes rotation {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
@keyframes rotate {
  0% {
    rotate: 0deg;
  }
  100% {
    rotate: 360deg;
  }
}
</style>
