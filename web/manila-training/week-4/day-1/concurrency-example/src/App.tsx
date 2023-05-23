import { FC, Suspense, lazy } from 'react'

import FilmList from './components/FilmList'
import './App.css'
import { useSelectedFilmId } from './AppState'

const FilmModal = lazy(() => import('./components/FilmModal'))

const App: FC = (): JSX.Element => {
  const selectedFilmId = useSelectedFilmId()

  return (
    <>
      <div className="filmList-container">
        <h1>Star Wars films:</h1>
        <Suspense fallback={<h3>Loading films...</h3>}>
          <FilmList />
        </Suspense>
      </div>
      {selectedFilmId && (
        <div className="filmModal">
          <div className="filmModal-content">
            <Suspense fallback={<h3>Loading film data...</h3>}>
              <FilmModal />
            </Suspense>
          </div>
        </div>
      )}
    </>
  )
}

export default App
