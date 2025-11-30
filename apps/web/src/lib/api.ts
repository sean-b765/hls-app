import { Configuration, MediaApiFp, MoviesApiFp, TVSeriesApiFp } from '@hls-app/sdk'

const config = new Configuration({
  basePath: 'http://localhost:8080',
})

export const mediaApi = MediaApiFp(config)

export const seriesApi = TVSeriesApiFp(config)

export const moviesApi = MoviesApiFp(config)
