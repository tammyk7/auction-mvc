import { FC } from 'react'
import { concat, of, switchMap } from 'rxjs'
import { SUSPENSE, bind } from '@react-rxjs/core'

import { fetchFilm } from '../services/SWApi'
import { selectedFilmId$, setSelectedFilmId } from '../AppState'

const filmData$ = selectedFilmId$.pipe(
  switchMap((filmId) => concat(of(SUSPENSE), fetchFilm(filmId)))
)

const [useFilmData] = bind(filmData$)

const FilmModal: FC = (): JSX.Element => {
  const { title, release_date, director, producer, opening_crawl } =
    useFilmData()

  return (
    <>
      <h2>{title}</h2>
      <h4>Released: {new Date(release_date).toDateString()}</h4>
      <h4>Directed by: {director}</h4>
      <h4>Produced by: {producer}</h4>
      <p>{opening_crawl}</p>
      <div className="closeModal" onClick={() => setSelectedFilmId('')}>
        Close
      </div>
    </>
  )
}

export default FilmModal
