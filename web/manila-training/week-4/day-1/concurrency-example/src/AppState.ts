import { bind } from '@react-rxjs/core'
import { BehaviorSubject } from 'rxjs'

export const selectedFilmId$ = new BehaviorSubject<string>('')
export const setSelectedFilmId = (filmId: string) => {
  selectedFilmId$.next(filmId)
}
export const [useSelectedFilmId] = bind(selectedFilmId$)

// Week 4 Day 2 code challenge: Add the code to set up the folling Observable and Functions:
// selectedCharacterId$
// setSelectedCharacterId
// useSelectedCharacterId
