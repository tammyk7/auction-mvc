import { useState, useEffect } from 'react'

export function useTimer(initialTime, isRunningAtStart) {
    const [isRunning, setIsRunning] = useState(isRunningAtStart)
    const [elapsedTime, setElapsedTime] = useState(initialTime)
  
    useEffect(() => {
      if (isRunning) {
        const startTime = Date.now() - elapsedTime
        const token = setInterval(() => setElapsedTime(Date.now() - startTime), 20)
        return () => clearInterval(token)
      }
    }, [isRunning])
  
  
    const toggleTime = () => setIsRunning(!isRunning)

    const resetTime = () => setElapsedTime(0)

    return [elapsedTime, isRunning, toggleTime, resetTime]
  }