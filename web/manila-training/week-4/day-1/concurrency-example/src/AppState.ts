import { bind } from '@react-rxjs/core'
import { BehaviorSubject } from 'rxjs'

export const selectedFilmId$ = new BehaviorSubject<string>('')
export const setSelectedFilmId = (newFilm: string) => {
  selectedFilmId$.next(newFilm)
}
export const [useSelectedFilmId] = bind(selectedFilmId$)
