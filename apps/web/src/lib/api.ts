import { type AuthRequest, type Media, type TvSeriesCollection } from '@hls-app/sdk'
import axios, { type AxiosResponse } from 'axios'

class BaseAPI {
  protected axios = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL,
    withCredentials: true,
  })

  protected baseURL() {
    return this.axios.defaults.baseURL
  }

  protected clearAuthorization() {
    localStorage.removeItem('access_token')
    delete this.axios.defaults.headers.common.Authorization
  }

  constructor() {
    this.axios.interceptors.request.use((config) => {
      const token = localStorage.getItem('access_token') || undefined
      if (token) {
        config.headers.Authorization = token
      } else {
        delete config.headers.Authorization
      }
      return config
    })
    this.axios.interceptors.response.use(
      (response) => {
        // Ensure jwt is cleared from localStorage if we were forbidden
        if (response.status === 403) this.clearAuthorization()
        return response
      },
      (error) => {
        const response = error.response as AxiosResponse
        if (response?.status === 403) this.clearAuthorization()
        return Promise.reject(error)
      },
    )
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

class UserAPI extends BaseAPI {
  public signup(body: AuthRequest) {
    return this.axios.post(`/api/user/signup`, body)
  }
  public login(body: AuthRequest) {
    return this.axios.post(`/api/user/login`, body)
  }
  public logout() {
    this.clearAuthorization()
  }
}

export const mediaApi = new MediaAPI()

export const seriesApi = new SeriesAPI()

export const moviesApi = new MoviesAPI()

export const userApi = new UserAPI()
