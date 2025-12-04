import { Configuration, MediaApiFp, MoviesApiFp, TVSeriesApiFp } from '@hls-app/sdk'

console.log(import.meta.env)
const config = new Configuration({
  basePath: import.meta.env.VITE_BASE_URL,
})

export const mediaApi = MediaApiFp(config)

export const seriesApi = TVSeriesApiFp(config)

export const moviesApi = MoviesApiFp(config)
