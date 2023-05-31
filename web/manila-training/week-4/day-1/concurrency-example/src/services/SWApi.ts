import { ajax } from 'rxjs/ajax'
import { Observable, retry, switchMap, timer } from 'rxjs'

const fetchData = <T>(endpoint: string, query?: string): Observable<T> => {
  const url = `https://swapi.dev/api/${endpoint}${
    query ? '/?search=' + query : ''
  }`

  return timer(1500).pipe(
    switchMap(() => ajax.getJSON<T>(url)),
    retry(1)
  )
}

export function fetchFilms(): Observable<FilmsApiResponse> {
  return fetchData<FilmsApiResponse>('films')
}

export function fetchFilm(id: string): Observable<Film> {
  return fetchData<Film>(`films/${id}`)
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

export interface Planet {
  climate: string
  created: Date
  diameter: string
  edited: Date
  films: string[]
  gravity: string
  name: string
  orbital_period: string
  population: string
  residents: string[]
  rotation_period: string
  surface_water: string
  terrain: string
  url: string
}

export interface Specie {
  average_height: string
  average_lifespan: string
  classification: string
  created: Date
  designation: string
  edited: Date
  eye_colors: string
  hair_colors: string
  homeworld: string | Planet
  language: string
  name: string
  people: string[]
  films: string[]
  skin_colors: string
  url: string
}

export interface Starship {
  MGLT: string
  cargo_capacity: string
  consumables: string
  cost_in_credits: string
  created: Date
  crew: string
  edited: Date
  hyperdrive_rating: string
  length: string
  manufacturer: string
  max_atmosphering_speed: string
  model: string
  name: string
  passengers: string
  films: string[]
  pilots: string[]
  starship_class: string
  url: string
}

export interface Vehicle {
  cargo_capacity: string
  consumables: string
  cost_in_credits: string
  created: Date
  crew: string
  edited: Date
  length: string
  manufacturer: string
  max_atmosphering_speed: string
  model: string
  name: string
  passengers: string
  pilots: string[]
  films: string[]
  url: string
  vehicle_class: string
}
