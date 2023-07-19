import React, { Suspense, lazy } from 'react'
import { Routes, Route } from 'react-router-dom'

import './App.css'
import CharacterList from './components/CharacterList'
import ErrorBoundary from './components/ErrorBoundary'
import { Subscribe } from '@react-rxjs/core'

const CharacterPage = lazy(() => import('./components/CharacterPage'))

function App() {
  return (
    <Suspense fallback={<h1>Loading...</h1>}>
      <ErrorBoundary>
        <Subscribe>
          <Routes>
            <Route path="*" element={<CharacterList />} />
            <Route path="/character/:id" element={<CharacterPage />} />
          </Routes>
        </Subscribe>
      </ErrorBoundary>
    </Suspense>
  )
}

export default App
