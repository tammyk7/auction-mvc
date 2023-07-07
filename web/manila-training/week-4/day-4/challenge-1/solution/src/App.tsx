import React, { Suspense, lazy } from 'react'
import { bind } from '@react-rxjs/core'
import { Routes, Route } from 'react-router-dom'

import './App.css'
import CharacterList from './components/CharacterList'
import ErrorBoundary from './components/ErrorBoundary'
import { fetchCharacters$ } from './services/thronesApi'

const CharacterPage = lazy(() => import('./components/CharacterPage'))

const characters$ = fetchCharacters$()
const [useCharacters] = bind(characters$)

function App() {
  const characters = useCharacters()

  return (
    <Suspense fallback={<h1>Loading...</h1>}>
      <ErrorBoundary>
        <Routes>
          <Route path="*" element={<CharacterList characters={characters} />} />
          <Route
            path="/character/:id"
            element={<CharacterPage characters={characters} />}
          />
        </Routes>
      </ErrorBoundary>
    </Suspense>
  )
}

export default App
