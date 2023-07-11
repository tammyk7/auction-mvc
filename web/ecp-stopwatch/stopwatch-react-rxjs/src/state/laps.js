import { shareLatest, state } from '@react-rxjs/core'
import { createSignal } from '@react-rxjs/utils'
import { combineLatest, map, merge, of, scan, startWith, switchMap, withLatestFrom } from 'rxjs'
import { elapsedTime$ } from './timer'

const [clearLaps$, clearLaps] = createSignal()
const [addLap$, addLap] = createSignal()
export { addLap, clearLaps }
const lapStart$ = merge(
  of(0),
  clearLaps$.pipe(map(() => 0)),
  addLap$.pipe(
    withLatestFrom(elapsedTime$),
    map(([, elapsedTime]) => elapsedTime)
  )
)
const currentLap$ = elapsedTime$.pipe(
  withLatestFrom(lapStart$),
  map(([elapsedTime, lapStart]) => {
    if (elapsedTime === 0) return null
    return elapsedTime - lapStart
  }),
  shareLatest()
)
const storedLaps$ = clearLaps$.pipe(
  startWith(null),
  switchMap(() =>
    addLap$.pipe(
      withLatestFrom(currentLap$),
      scan(
        ({ laps, min, max }, [, current]) => {
          if (laps.length === 0)
            return {
              laps: [{ time: current, lapNumber: 1 }],
              min: 0,
              max: 0,
            }
          const newValue = { laps: [...laps], min, max }
          newValue.laps.push({ time: current, lapNumber: laps.length + 1 })
          if (laps[min].time > current) {
            newValue.min = laps.length
          }
          if (laps[max].time < current) {
            newValue.max = laps.length
          }
          return newValue
        },
        { laps: [], min: 0, max: 0 }
      ),
      startWith({ laps: [], min: 0, max: 0 })
    )
  )
)

export const laps$ = state(
  combineLatest([currentLap$, storedLaps$]).pipe(
    map(([currentLapTime, { laps, min, max }]) => {
      if (currentLapTime === null) return { laps: [], min: 0, max: 0 }

      return {
        laps: [...laps, { time: currentLapTime, lapNumber: laps.length + 1 }].reverse(),
        minLap: laps[min],
        maxLap: laps[max],
      }
    })
  ),
  { laps: [], min: 0, max: 0 }
)
