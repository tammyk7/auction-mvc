import { state } from '@react-rxjs/core'
import { createSignal } from '@react-rxjs/utils'
import { animationFrames, defer, EMPTY, map, merge, scan, switchMap, withLatestFrom } from 'rxjs'

const [toggleTimer$, toggleTimer] = createSignal()
const [resetTimer$, resetTimer] = createSignal()
export { toggleTimer, resetTimer }

export const isRunning$ = state(toggleTimer$.pipe(scan((acc) => !acc, false)), false)
export const elapsedTime$ = state(
  merge(
    resetTimer$.pipe(map(() => 0)),
    isRunning$.pipe(
      withLatestFrom(defer(() => elapsedTime$)),
      switchMap(([isRunning, lastElapsedTime]) => {
        if (isRunning) {
          const startTime = Date.now() - lastElapsedTime
          return animationFrames().pipe(map(() => Date.now() - startTime))
        } else {
          return EMPTY
        }
      })
    )
  ),
  0
)
