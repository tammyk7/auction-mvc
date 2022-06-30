export const initialState = {
  isRunning: false,
  elapsedTime: 0,
  totalLapTime: 0,
  laps: [],
  minLap: { time: Number.MAX_VALUE, lapNumber: 0 },
  maxLap: { time: 0, lapNumber: 0 },
}

export const Actions = {
  TOGGLE_TIMER: 'TOGGLE_TIMER',
  RESET_TIMER: 'RESET_TIMER',
  ADD_LAP: 'ADD_LAP',
  UPDATE_TIME: 'UPDATE_TIME',
}

export default function reducer(state, action) {
    console.log(state, action)
  switch (action.type) {
    case Actions.TOGGLE_TIMER:
      return { ...state, isRunning: !state.isRunning }

    case Actions.RESET_TIMER:
      return initialState

    case Actions.ADD_LAP: {
      const { elapsedTime, totalLapTime, laps, minLap, maxLap } = state

      const previousLap = {
        ...laps[0],
        time: elapsedTime - totalLapTime,
      }

      const newLap = {
        lapNumber: laps.length + 1,
        time: 0,
      }

      return {
        ...state,
        totalLapTime: totalLapTime + previousLap.time,
        laps: [newLap, previousLap, ...laps.slice(1)],
        minLap: previousLap.time < minLap.time ? previousLap : minLap,
        maxLap: previousLap.time > maxLap.time ? previousLap : maxLap,
      }
    }

    case Actions.UPDATE_TIME: {
      const { laps, totalLapTime } = state
      const { updatedTime } = action
      return {
        ...state,
        elapsedTime: updatedTime,
        laps: [
          {
            ...(laps[0] ?? { lapNumber: 1 }), // spread (first array element or, if null, initial lap)
            time: updatedTime - totalLapTime,
          },
          ...laps.slice(1),
        ],
      }
    }

    default:
      throw Error('Unrecognised action type in reducer', action)
  }
}
