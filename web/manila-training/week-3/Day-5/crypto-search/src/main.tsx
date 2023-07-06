import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.tsx'
import './index.css'
import { Subscribe } from 'react-rxjs'

ReactDOM.createRoot(document.getElementById('root') as HTMLElement).render(
  <React.StrictMode>
    <Subscribe>
      <App />
    </Subscribe>
  </React.StrictMode>,
)
