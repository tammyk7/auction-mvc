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
    <div className="filmList">
      {films.results.map((film) => {
        return (
          <div
            key={film.episode_id}
            onClick={() => setSelectedFilmId(getIdFromSWApiURL(film.url))}
          >
            {film.title}
          </div>
        )
      })}
    </div>
  )
}

export default FilmList
