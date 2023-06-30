import { useEffect, useRef, useState } from 'react'
import { fromEvent, interval, takeWhile } from 'rxjs'

import '../App.css'
import { formatTime } from '../utils/formatTime'

// Create an Observable that emmits every second
const seconds$ = interval(1000)

function App() {
  const [time, setTime] = useState(0)
  const [isRunning, setIsRunning] = useState(false)

  const startButtonRef = useRef<HTMLButtonElement>(null)
  const stopButtonRef = useRef<HTMLButtonElement>(null)

  useEffect(() => {
    if (startButtonRef.current === null || stopButtonRef.current === null)
      return

    // Create Observables that will emit the start and stop button click events.
    const startClicks$ = fromEvent(startButtonRef.current, 'click')
    const stopClicks$ = fromEvent(stopButtonRef.current, 'click')

    // This subscription will change the isRunning state to true when the startClicks$ Observable emits.
    const startClicksSub = startClicks$.subscribe(() => {
      setIsRunning(true)
    })

    // This subscription will change the isRunning state to false when the stopClicks$ Observable emits.
    const stopClicksSub = stopClicks$.subscribe(() => {
      setIsRunning(false)
    })

    return () => {
      startClicksSub.unsubscribe()
      stopClicksSub.unsubscribe()
    }
  }, [startButtonRef, stopButtonRef])

  useEffect(() => {
    // This subscription will only subscribe to the seconds$ Observable when isRunning is true.
    // Within the subscription we increment the time state when seconds$ emits.
    const secondsSubscription$ = seconds$
      .pipe(takeWhile(() => isRunning))
      .subscribe(() => {
        setTime((currentTime) => ++currentTime)
      })

    return () => {
      secondsSubscription$.unsubscribe()
    }
  }, [isRunning])

  return (
    <>
      <h1>{formatTime(time)}</h1>
      <div>
        <button ref={startButtonRef}>Start</button>
        <button ref={stopButtonRef}>Stop</button>
      </div>
    </>
  )
}

export default App
