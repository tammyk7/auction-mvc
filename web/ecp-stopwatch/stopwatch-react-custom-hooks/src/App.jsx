import { formatTime } from './utils'
import Buttons from "./components/Buttons";
import LapTable from "./components/LapTable";
import { useTimer } from './hooks/useTimer'
import { useLapRecorder } from './hooks/useLapRecorder';
import './App.css'

function App() {
  const [elapsedTime, isRunning, toggleTimer, resetTimer] = useTimer(Date.now())
  const [laps, minLap, maxLap, addLap, clearLaps] = useLapRecorder(elapsedTime)

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
