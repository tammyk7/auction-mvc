import { ajax } from 'rxjs/ajax'
import { Observable } from 'rxjs'

const fetchData$ = <T>(endpoint: string): Observable<T> => {
  const url = `/thrones/${endpoint}`

  return ajax.getJSON<T>(url)
}

export const fetchCharacters$ = (): Observable<Character[]> => {
  return fetchData$<Character[]>('Characters')
}

export const fetchCharacter = (id: number): Observable<Character> => {
  return fetchData$<Character>(`Characters/${id}`)
}

export interface Character {
  id?: number
  firstName?: string
  lastName?: string
  fullName?: string
  title?: string
  family?: string
  image?: string
  imageUrl?: string
}
