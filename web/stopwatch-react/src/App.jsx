import { useState, useEffect } from 'react'
import { formatTime } from './utils'
import './App.css'

const initialLapData = {
  totalLapTime: 0,
  laps: [],
  minLap: { time: Number.MAX_VALUE, lapNumber: 0 },
  maxLap: { time: 0, lapNumber: 0 },
}

function App() {
  const [isRunning, setIsRunning] = useState(false)
  const [elapsedTime, setElapsedTime] = useState(0)
  const [lapData, setLapData] = useState(initialLapData)

  useEffect(() => {
    if (isRunning) {
      const startTime = Date.now() - elapsedTime
      const token = setInterval(() => setElapsedTime(Date.now() - startTime), 10)
      return () => clearInterval(token)
    }
  }, [isRunning])

  useEffect(() => {
    if (elapsedTime > 0) {
      const currentLap = lapData.laps[0] ?? { lapNumber: 1 }
      const updatedLap = {
        ...currentLap,
        time: elapsedTime - lapData.totalLapTime,
      }
      setLapData({
        ...lapData,
        laps: [updatedLap, ...lapData.laps.slice(1)],
      })
    }
  }, [elapsedTime])

  const toggleTimer = () => setIsRunning(!isRunning)

  const resetTimer = () => {
    setIsRunning(false)
    setElapsedTime(0)
    setLapData(initialLapData)
  }

  const addLap = () => {
    const { totalLapTime, laps, minLap, maxLap } = lapData

    const previousLap = {
      ...laps[0],
      time: elapsedTime - totalLapTime,
    }

    const newLap = {
      lapNumber: lapData.laps.length + 1,
      time: 0,
    }

    setLapData({
      totalLapTime: totalLapTime + previousLap.time,
      laps: [newLap, previousLap, ...laps.slice(1)],
      minLap: previousLap.time < minLap.time ? previousLap : minLap,
      maxLap: previousLap.time > maxLap.time ? previousLap : maxLap,
    })
  }

  const stopStartText = isRunning ? 'Stop' : 'Start'
  const lapResetText = !isRunning && elapsedTime > 0 ? 'Reset' : 'Lap'
  const isLapResetDisabled = !isRunning && elapsedTime === 0

  return (
    <div>
      <h2>{formatTime(elapsedTime)}</h2>
      <div>
        <button onClick={isRunning ? addLap : resetTimer} disabled={isLapResetDisabled}>
          {lapResetText}
        </button>
        <button onClick={toggleTimer}>{stopStartText}</button>
      </div>
      <table>
        <tbody>
          {lapData.laps.map((lap) => (
            <tr key={lap.lapNumber}>
              <td>Lap {lap.lapNumber}</td>
              <td>{formatTime(lap.time)}</td>
              {lapData.laps.length > 2 ? (
                <td>
                  {/* TODO: replace with CSS class names for min/max colours */}
                  {lap.lapNumber === lapData.minLap.lapNumber ? 'min' : null}
                  {lap.lapNumber === lapData.maxLap.lapNumber ? 'max' : null}
                </td>
              ) : null}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}

export default App
