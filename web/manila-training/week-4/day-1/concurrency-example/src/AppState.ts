import { bind } from '@react-rxjs/core'
import { createSignal } from '@react-rxjs/utils'

const [filmId$, setSelectedFilmId] = createSignal<string>()
const [useSelectedFilmId, selectedFilmId$] = bind(filmId$, '')

// Week 4 Day 2 code challenge: Add the code to set up the folling Observable and Functions:
// selectedCharacterId$
// setSelectedCharacterId
// useSelectedCharacterId

export { setSelectedFilmId, useSelectedFilmId, selectedFilmId$ }
