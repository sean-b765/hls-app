import { Configuration, MediaApiFp, MoviesApiFp, TVSeriesApiFp, UserApiFp } from '@hls-app/sdk'

const config = new Configuration({
  basePath: import.meta.env.VITE_BASE_URL,
  accessToken: () => {
    const token = localStorage.getItem('access_token') ?? ''
    if (!token) {
      console.log('NO TOKEN :(')
    }
    return token
  },
})

export const mediaApi = MediaApiFp(config)

export const seriesApi = TVSeriesApiFp(config)

export const moviesApi = MoviesApiFp(config)

export const userApi = UserApiFp(config)
