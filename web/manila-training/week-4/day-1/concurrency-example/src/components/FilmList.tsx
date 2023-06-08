import { FC } from 'react'
import { bind } from '@react-rxjs/core'

import { fetchFilms } from '../services/SWApi'
import { setSelectedFilmId } from '../AppState'
import { getIdFromSWApiURL } from '../utils/getIdFromSWApiURL'

const films$ = fetchFilms()
const [useFilms] = bind(films$)

const FilmList: FC = (): JSX.Element => {
  const films = useFilms()

  return (
    <div className="list">
      {films.results.map((film) => {
        return (
          <h3
            key={film.episode_id}
            onClick={() => setSelectedFilmId(getIdFromSWApiURL(film.url))}
          >
            {film.title}
          </h3>
        )
      })}
    </div>
  )
}

export default FilmList
