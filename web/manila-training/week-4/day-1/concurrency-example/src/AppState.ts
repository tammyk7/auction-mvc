import { bind } from '@react-rxjs/core'
import { BehaviorSubject } from 'rxjs'

export const selectedFilmId$ = new BehaviorSubject<string>('')
export const setSelectedFilmId = (filmId: string) => {
  selectedFilmId$.next(filmId)
}
export const [useSelectedFilmId] = bind(selectedFilmId$)
