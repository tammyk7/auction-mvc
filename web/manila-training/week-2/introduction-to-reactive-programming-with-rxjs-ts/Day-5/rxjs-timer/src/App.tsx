import { useState } from 'react'

import './App.css'
import { formatTime } from './utils/formatTime'

function App() {
  const [time, setTime] = useState(0)
  const [isRunning, setIsRunning] = useState(false)

  return (
    <>
      <h1>{formatTime(time)}</h1>
      <div>
        <button>Start</button>
        <button>Stop</button>
      </div>
    </>
  )
}

export default App
