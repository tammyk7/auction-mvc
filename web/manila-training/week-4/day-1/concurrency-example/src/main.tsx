import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { Subscribe } from '@react-rxjs/core'

import App from './App.tsx'
import './index.css'

const root = createRoot(document.getElementById('root') as HTMLElement)

root.render(
  <StrictMode>
    <Subscribe>
      <App />
    </Subscribe>
  </StrictMode>
)
