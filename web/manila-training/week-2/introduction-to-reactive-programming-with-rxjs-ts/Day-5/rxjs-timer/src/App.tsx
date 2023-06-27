import { useEffect, useRef, useState } from 'react'
import { Subject, fromEvent, interval, takeUntil } from 'rxjs'

import './App.css'
import { formatTime } from './utils/formatTime'

function App() {
  const [time, setTime] = useState(0)
  const [isRunning, setIsRunning] = useState(false)
  const [addColor, setAddColor] = useState(false)

  const startButtonRef = useRef<any>(null)
  const stopButtonRef = useRef<any>(null)

  useEffect(() => {
    const unsubscribe$ = new Subject<number>()

    interval(1000)
      .pipe(takeUntil(unsubscribe$))
      .subscribe(() => {
        if (isRunning) {
          setTime((currentTime) => currentTime + 1)
        }
      })

    return () => {
      unsubscribe$.next(1)
      unsubscribe$.complete()
    }
  }, [isRunning])

  useEffect(() => {
    const startClicks$ = fromEvent(startButtonRef.current, 'click')
    const stopClicks$ = fromEvent(stopButtonRef.current, 'click')

    const startClicksSub = startClicks$.subscribe(() => {
      setIsRunning(true)
    })

    const stopClicksSub = stopClicks$.subscribe(() => {
      setIsRunning(false)
    })

    return () => {
      startClicksSub.unsubscribe()
      stopClicksSub.unsubscribe()
    }
  }, [startButtonRef, stopButtonRef])

  useEffect(() => {
    if (time % 5 === 0 && time !== 0) {
      setAddColor(true)
    } else {
      setAddColor(false)
    }
  }, [time])

  return (
    <>
      <h1 className={`time ${addColor ? 'colored' : ''}`}>
        {formatTime(time)}
      </h1>
      <div className="controls">
        <button ref={startButtonRef}>Start</button>
        <button ref={stopButtonRef}>Stop</button>
      </div>
    </>
  )
}

export default App
