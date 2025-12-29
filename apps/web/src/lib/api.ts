import type { JwtPayload } from '@/types/user'
import { type AuthRequest, type Library, type Media, type TvSeries } from '@hls-app/sdk'
import axios, { type AxiosInstance, type AxiosResponse, type AxiosResponseHeaders } from 'axios'
import { jwtDecode } from 'jwt-decode'
import { emitter } from './event'
import type { FolderNode } from '@/types/filesystem'
import type { Endpoints } from '@/types/common'

class BaseAPI {
  protected axios!: AxiosInstance

  protected registerAxios() {
    if (this.axios !== undefined) return
    this.axios = axios.create({
      baseURL: import.meta.env.VITE_BASE_URL,
      withCredentials: true,
    })
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
        this.saveHeaders(response.headers)
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

  protected getAccessToken() {
    return localStorage.getItem('access_token')
  }

  protected getJwt() {
    const token = this.getAccessToken()
    if (!token) return null
    return jwtDecode<JwtPayload>(token)
  }

  protected async refreshToken() {
    this.clearAuthorization()
    try {
      const response = await this.axios.post('/auth/refresh')
      if (response.status !== 200) {
        emitter.emit('auth', null)
      }
    } catch {
      emitter.emit('auth', null)
    }
  }

  protected clearAuthorization() {
    localStorage.removeItem('access_token')
    delete this.axios.defaults.headers.common.Authorization
  }

  protected saveHeaders(headers: Partial<AxiosResponseHeaders>) {
    if (headers['authorization']) {
      // If an Authorization header was sent from the backend, this means we have been authenticated
      const jwt = headers['authorization']
      localStorage.setItem('access_token', 'Bearer ' + jwt)
      emitter.emit('auth', this.getJwt())
    }
    if (headers['x-hls-token']) {
      const hlsToken = headers['x-hls-token']
      sessionStorage.setItem('hls_token', hlsToken)
    }
  }

  protected refreshOrLogoutIfNecessary(response: AxiosResponse) {
    if (!response) return
    if (response.status !== 403) return

    if (response.config.url !== '/auth/refresh') {
      // Try to use the refresh token
      this.refreshToken()
      return
    }
    // Ensure jwt is cleared from localStorage if we were forbidden and refresh token was unsuccessfully refreshed
    this.clearAuthorization()
    emitter.emit('auth', null)
  }

  constructor() {
    this.registerAxios()
  }
}

export class AuthAPI extends BaseAPI {
  /**
   * Setup the auth tokens
   */
  public async setup() {
    const jwt = this.getJwt()
    if (!jwt) return emitter.emit('auth', null)

    const nowEpochSeconds = Date.now() / 1000
    if (jwt.exp > nowEpochSeconds) {
      emitter.emit('auth', jwt)
      return
    }

    if (jwt.exp <= nowEpochSeconds) {
      await this.refreshToken()
    }
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

  public async generateHlsToken(mediaId: string) {
    const response = await this.axios.post(`/api/playlist/${mediaId}`)
    console.log(response.headers['x-hls-token'])
  }
}

export class CrudAPI<T> extends BaseAPI {
  public endpoint: Endpoints

  constructor(endpoint: Endpoints) {
    super()
    this.endpoint = endpoint
  }

  public create(library: T) {
    return this.axios.post<T>(this.endpoint, library)
  }
  public upsert(library: T) {
    return this.axios.put<T>(this.endpoint, library)
  }
  public findAll() {
    return this.axios.get<T[]>(this.endpoint)
  }
  public findById(id: string) {
    return this.axios.get<T>(`${this.endpoint}/${id}`)
  }
  public findByIds(ids: string[]) {
    return this.axios.post<T>(`${this.endpoint}/fetch`, ids)
  }
  public deleteById(id: string) {
    return this.axios.delete(`${this.endpoint}/${id}`)
  }
  public deleteByIds(id: string) {
    return this.axios.delete(`${this.endpoint}/${id}`)
  }
}

class MediaAPI extends CrudAPI<Media> {
  constructor() {
    super('/api/media')
  }
}

class SeriesAPI extends CrudAPI<TvSeries> {
  constructor() {
    super('/api/series')
  }
}

class LibraryAPI extends CrudAPI<Library> {
  constructor() {
    super('/api/library')
  }
  public scan(id: string) {
    return this.axios.post(`${this.endpoint}/${id}/scan`)
  }
}

class FileSystemAPI extends BaseAPI {
  public findFolders(path: string) {
    return this.axios.get<FolderNode[]>(`/api/filesystem/folders?path=${encodeURIComponent(path)}`)
  }
}

export const authApi = new AuthAPI()
export const mediaApi = new MediaAPI()
export const seriesApi = new SeriesAPI()
export const fileSystemApi = new FileSystemAPI()
export const libraryApi = new LibraryAPI()
