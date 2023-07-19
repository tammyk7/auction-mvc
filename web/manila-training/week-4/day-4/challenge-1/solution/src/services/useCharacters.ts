import { bind } from '@react-rxjs/core'
import { fetchCharacters$ } from './thronesApi'

const characters$ = fetchCharacters$()
export const [useCharacters] = bind(characters$)
