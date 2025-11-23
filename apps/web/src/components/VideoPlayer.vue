<script setup lang="ts">
import Hls from 'hls.js'
import { onMounted, useTemplateRef } from 'vue'

const url = 'http://localhost:8080/api/playlist/1'

const player = useTemplateRef<HTMLVideoElement>('player')
let hls: Hls

function start() {
  if (player.value == null) return

  if (Hls.isSupported()) {
    hls = new Hls()
    hls.attachMedia(player.value)
    hls.on(Hls.Events.MEDIA_ATTACHED, function () {
      hls.on(Hls.Events.MANIFEST_PARSED, function (event, data) {
        console.log('manifest loaded with ' + data.levels.length + ' quality level(s)')
        player.value!.play()
      })

      hls.loadSource(url)
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
    player.value.src = url
    player.value.addEventListener('loadedmetadata', function () {
      player.value!.play()
    })
  } else {
    throw new Error('hls not supported')
  }
}

onMounted(() => {
  start()
})
</script>

<template>
  <div class="video-wrapper">
    <div class="video-controls">
      <button>PAUSE</button>
      <button>PLAY</button>
    </div>
    <video
      ref="player"
      id="video-player"
      width="600"
      height="400"
      style="max-height: 400px; max-width: 600px"
    ></video>
  </div>
</template>
