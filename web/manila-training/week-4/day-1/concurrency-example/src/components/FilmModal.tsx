import { FC } from 'react'
import { concat, filter, of, switchMap } from 'rxjs'
import { SUSPENSE, bind } from '@react-rxjs/core'

import { fetchFilm } from '../services/SWApi'
import { selectedFilmId$, setSelectedFilmId } from '../AppState'

const filmData$ = selectedFilmId$.pipe(
  filter((filmId) => !!filmId),
  switchMap((filmId) => concat(of(SUSPENSE), fetchFilm(filmId)))
)

const [useFilmData] = bind(filmData$)

const FilmModal: FC = (): JSX.Element => {
  const { title, release_date, director, producer, opening_crawl } =
    useFilmData()

  return (
    <>
      <h2>{title}</h2>
      <p>
        <strong>Released: </strong>
        {release_date ? new Date(release_date).toDateString() : ''}
      </p>
      <p>
        <strong>Directed by: </strong>
        {director}
      </p>
      <p>
        <strong>Produced by: </strong>
        {producer}
      </p>
      <p>{opening_crawl}</p>
      <div className="closeModal" onClick={() => setSelectedFilmId('')}>
        Close
      </div>
    </>
  )
}

export default FilmModal
