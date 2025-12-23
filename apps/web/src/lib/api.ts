import type { JwtPayload } from '@/types/user'
import { type AuthRequest, type Media, type TvSeriesCollection } from '@hls-app/sdk'
import axios, { type AxiosResponse } from 'axios'
import { jwtDecode } from 'jwt-decode'
import { emitter } from './event'

class BaseAPI {
  protected axios = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL,
    withCredentials: true,
  })

  protected baseURL() {
    return this.axios.defaults.baseURL
  }

  protected getAccessToken() {
    return localStorage.getItem('access_token')
  }

  protected getJwt() {
    const token = this.getAccessToken()
    if (!token) return null
    return jwtDecode<JwtPayload>(token)
  }

  protected async consumeRefreshToken() {
    this.clearAuthorization()
    try {
      const response = await this.axios.post('/auth/refresh')
      if (response.status !== 200) {
        emitter.emit('auth', null)
      }
    } catch (e) {
      emitter.emit('auth', null)
    }
  }

  protected clearAuthorization() {
    localStorage.removeItem('access_token')
    delete this.axios.defaults.headers.common.Authorization
  }

  protected refreshOrLogoutIfNecessary(response: AxiosResponse) {
    if (!response) return
    if (response.status !== 403) return

    if (response.config.url !== '/auth/refresh') {
      // Try to use the refresh token
      this.consumeRefreshToken()
      return
    }
    // Ensure jwt is cleared from localStorage if we were forbidden and refresh token was unsuccessfully refreshed
    this.clearAuthorization()
    emitter.emit('auth', null)
  }

  constructor() {
    this.axios.interceptors.request.use((config) => {
      const token = this.getAccessToken()
      if (token) {
        config.headers.Authorization = token
      } else {
        delete config.headers.Authorization
      }
      return config
    })
    this.axios.interceptors.response.use(
      (response) => {
        if (response.headers['authorization']) {
          const jwt = response.headers['authorization']
          localStorage.setItem('access_token', 'Bearer ' + jwt)
          emitter.emit('auth', this.getJwt())
        }

        this.refreshOrLogoutIfNecessary(response)
        return response
      },
      (error) => {
        const response = error.response as AxiosResponse
        this.refreshOrLogoutIfNecessary(response)
        return Promise.reject(error)
      },
    )
  }
}

export class AuthAPI extends BaseAPI {
  /**
   * Setup the auth tokens on initial construction
   */
  protected async setup() {
    const jwt = this.getJwt()
    if (!jwt) return

    const nowEpochSeconds = Date.now() / 1000
    if (jwt.exp > nowEpochSeconds) {
      emitter.emit('auth', jwt)
      return
    }

    if (jwt.exp <= nowEpochSeconds) {
      await this.consumeRefreshToken()
    }
  }

  constructor() {
    super()
    this.setup()
  }

  public signup(body: AuthRequest) {
    return this.axios.post(`/auth/signup`, body)
  }
  public login(body: AuthRequest) {
    return this.axios.post(`/auth/login`, body)
  }
  public async logout() {
    await this.axios.post(`/auth/logout`)
    this.clearAuthorization()
    emitter.emit('auth', null)
  }
}

class MediaAPI extends BaseAPI {
  public getAll() {
    return this.axios.get<Media[]>(`/api/media`)
  }
  public getById(id: string) {
    return this.axios.get<Media>(`/api/media/${id}`)
  }
}

class SeriesAPI extends BaseAPI {
  public findTvSeries() {
    return this.axios.get<TvSeriesCollection[]>(`/api/series`)
  }
}

class MoviesAPI extends BaseAPI {
  public findMovies() {
    return this.axios.get<Media[]>(`/api/movies`)
  }
}

export const mediaApi = new MediaAPI()

export const seriesApi = new SeriesAPI()

export const moviesApi = new MoviesAPI()
