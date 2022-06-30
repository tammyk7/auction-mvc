import { useEffect, useReducer } from 'react'
import { formatTime } from './utils'
import reducer, { Actions, initialState } from "./reducers/stopwatchReducer";
import Buttons from "./components/Buttons";
import LapTable from "./components/LapTable";
import './App.css'

function App() {
  const [state, dispatch] = useReducer(reducer, initialState)

  function updateTime(startTime) {
    dispatch({ type: Actions.UPDATE_TIME, updatedTime: Date.now() - startTime })
  }

  useEffect(() => {
    if (state.isRunning) {
      const startTime = Date.now() - state.elapsedTime
      const token = setInterval(() => updateTime(startTime), 10)
      return () => clearInterval(token)
    }
  }, [state.isRunning])

  const toggleTimer = () => dispatch({ type: Actions.TOGGLE_TIMER })
  const addLap = () => dispatch({ type: Actions.ADD_LAP })
  const resetTimer = () => dispatch({ type: Actions.RESET_TIMER })

  return (
    <div>
      <h2>{formatTime(state.elapsedTime)}</h2>

      <Buttons
        isRunning={state.isRunning}
        isAtZero={state.elapsedTime === 0}
        startAction={toggleTimer}
        stopAction={toggleTimer}
        resetAction={resetTimer}
        lapAction={addLap}
      />

      <LapTable laps={state.laps} minLap={state.minLap} maxLap={state.maxLap} />
    </div>
  )
}

export default App
