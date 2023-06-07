import { FC, Suspense, lazy } from 'react'

import FilmList from '../components/FilmList'
import { useSelectedFilmId } from '../AppState'
import Loading from '../components/Loading'

const FilmModal = lazy(() => import('../components/FilmModal'))

const Films: FC = (): JSX.Element => {
  const selectedFilmId = useSelectedFilmId()

  return (
    <>
      <div className="page">
        <h1>Star Wars films:</h1>
        <Suspense fallback={<Loading message="films" />}>
          <FilmList />
        </Suspense>
      </div>
      {selectedFilmId && (
        <div className="modal">
          <div className="modal-content">
            <Suspense fallback={<Loading message="film data" />}>
              <FilmModal />
            </Suspense>
          </div>
        </div>
      )}
    </>
  )
}

export default Films
