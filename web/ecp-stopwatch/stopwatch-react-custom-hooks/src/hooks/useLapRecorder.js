import { useState, useEffect } from 'react'

const initialLapData = {
  totalLapTime: 0,
  laps: [],
  minLap: { time: Number.MAX_VALUE, lapNumber: 0 },
  maxLap: { time: 0, lapNumber: 0 },
}

export function useLapRecorder(elapsedTime) {
  const [lapData, setLapData] = useState(initialLapData)

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

  const clearLaps = () => setLapData(initialLapData)

  return [lapData.laps, lapData.minLap, lapData.maxLap, addLap, clearLaps]
}
