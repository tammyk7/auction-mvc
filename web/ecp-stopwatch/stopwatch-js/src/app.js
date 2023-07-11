import { startTimer, stopTimer, resetTimer, isRunning, elapsedTime } from './timer.js'
import { createFirstLap, addLap, resetLaps } from './laps.js'

const startStopButton = document.getElementById('startStopButton')
const lapResetButton = document.getElementById('lapResetButton')

startStopButton.onclick = () => {
  if (!isRunning) {
    if (elapsedTime === 0) {
      createFirstLap()
    }
    startTimer()
    startStopButton.innerText = 'Stop'
    startStopButton.className = 'started'
    lapResetButton.innerText = 'Lap'
    lapResetButton.disabled = false
  } else {
    stopTimer()
    startStopButton.innerText = 'Start'
    startStopButton.className = 'stopped'
    lapResetButton.innerText = 'Reset'
  }
}

lapResetButton.onclick = () => {
  if (isRunning) {
    addLap(elapsedTime)
  } else {
    resetTimer()
    resetLaps()
    startStopButton.innerText = 'Start'
    lapResetButton.innerText = 'Lap'
    lapResetButton.disabled = true
  }
}
