import { ajax } from 'rxjs/ajax'
import { Observable, retry, switchMap, timer } from 'rxjs'

const fetchData = <T>(endpoint: string): Observable<T> => {
  const url = `https://swapi.dev/api/${endpoint}`

  return timer(1500).pipe(
    switchMap(() => ajax.getJSON<T>(url)),
    retry(1)
  )
}

export const fetchFilms = (): Observable<FilmsApiResponse> => {
  return fetchData<FilmsApiResponse>('films')
}

export const fetchFilm = (id: string): Observable<Film> => {
  return fetchData<Film>(`films/${id}`)
}

export interface FilmsApiResponse {
  count: number
  next: string | null
  previous: string | null
  results: Film[]
}

export interface Film {
  characters: string[]
  created: Date
  director: string
  edited: Date
  episode_id: string
  opening_crawl: string
  planets: string[]
  producer: string
  release_date: string
  species: string[]
  starships: string[]
  title: string
  url: string
  vehicles: string[]
}
