import { Configuration, MediaApiFp } from '@hls-app/sdk'

export const mediaApi = MediaApiFp(
  new Configuration({
    basePath: 'http://localhost:8080',
  }),
)
