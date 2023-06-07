import { FC, lazy, Suspense } from 'react'

import './App.css'
import Loading from './components/Loading'
import FilmList from './components/FilmList'
import { useSelectedFilmId } from './AppState'

const FilmModal = lazy(() => import('./components/FilmModal'))

const App: FC = (): JSX.Element => {
  const selectedFilmId = useSelectedFilmId()

  return (
    <div className="page">
      <h1>Star Wars films:</h1>
      <Suspense fallback={<Loading message="films" />}>
        <FilmList />
      </Suspense>
      {selectedFilmId && (
        <div className="modal">
          <div className="modal-content">
            <Suspense fallback={<Loading message="film data" />}>
              <FilmModal />
            </Suspense>
          </div>
        </div>
      )}
    </div>
  )
}

export default App
