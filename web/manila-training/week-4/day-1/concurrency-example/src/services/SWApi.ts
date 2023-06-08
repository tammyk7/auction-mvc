import { ajax } from 'rxjs/ajax'
import { Observable } from 'rxjs'

const fetchData = <T>(endpoint: string): Observable<T> => {
  const url = `https://swapi.dev/api/${endpoint}`

  return ajax.getJSON<T>(url)
}

export const fetchFilms = (): Observable<FilmsApiResponse> => {
  return fetchData<FilmsApiResponse>('films')
}

export const fetchFilm = (id: string): Observable<Film> => {
  return fetchData<Film>(`films/${id}`)
}

export const fetchCharacters = (): Observable<PeopleApiResponse> => {
  return fetchData<PeopleApiResponse>('people')
}

export const fetchCharacter = (id: string): Observable<Person> => {
  return fetchData<Person>(`people/${id}`)
}

export interface FilmsApiResponse {
  count: number
  next: string | null
  previous: string | null
  results: Film[]
}

export interface PeopleApiResponse {
  count: number
  next: string | null
  previous: string | null
  results: Person[]
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

export interface Person {
  birth_year: string
  eye_color: string
  films: string[]
  gender: string
  hair_color: string
  height: string
  homeworld: string
  mass: string
  name: string
  skin_color: string
  created: Date
  edited: Date
  species: string[]
  starships: string[]
  url: string
  vehicles: string[]
}
