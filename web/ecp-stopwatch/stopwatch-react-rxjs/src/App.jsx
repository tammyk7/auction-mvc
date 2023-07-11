import { formatTime } from './utils'
import Buttons from "./components/Buttons";
import LapTable from "./components/LapTable";
import { elapsedTime$, isRunning$, resetTimer, toggleTimer } from './state/timer';
import { laps$, clearLaps, addLap } from './state/laps';
import './App.css'
import { useStateObservable } from '@react-rxjs/core';

function App() {
  const elapsedTime = useStateObservable(elapsedTime$);
  const isRunning = useStateObservable(isRunning$);
  const {laps, minLap, maxLap} = useStateObservable(laps$);

  const reset = () => {
    resetTimer(),
    clearLaps()
  }

  return (
    <div>
      <h2>{formatTime(elapsedTime)}</h2>

      <Buttons
        isRunning={isRunning}
        isAtZero={elapsedTime === 0}
        startAction={toggleTimer}
        stopAction={toggleTimer}
        resetAction={reset}
        lapAction={addLap}
      />

      <LapTable laps={laps} minLap={minLap} maxLap={maxLap} />
    </div>
  )
}

export default App
