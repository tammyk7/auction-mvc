import React, { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { Subscribe } from '@react-rxjs/core'
import { BrowserRouter } from 'react-router-dom'

import App from './App'

const root = createRoot(document.getElementById('root') as HTMLElement)

root.render(
  <StrictMode>
    <BrowserRouter>
      <Subscribe>
        <App />
      </Subscribe>
    </BrowserRouter>
  </StrictMode>
)
