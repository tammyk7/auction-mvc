import { connectToGateway, consoleInterceptor } from '@adaptive/hydra-platform'
import { backEndUrl } from 'config'
import React, { Suspense } from 'react'
import ReactDOM from 'react-dom'
import CssBaseline from '@material-ui/core/CssBaseline'
import { ThemeProvider } from './component-library'
import { App } from './App'
import { Subscribe } from '@react-rxjs/core'

ReactDOM.render(
  <React.StrictMode>
    <ThemeProvider>
      <Suspense fallback={<h1>Loading...</h1>}>
        <React.Fragment>
          <CssBaseline />
          <Subscribe>
            <App />
          </Subscribe>
        </React.Fragment>
      </Suspense>
    </ThemeProvider>
  </React.StrictMode>,
  document.getElementById('root')
)

const useJson = true

connectToGateway({
  url: backEndUrl,
  protocolVersion: undefined,
  interceptor: consoleInterceptor,
  useJson,
})
