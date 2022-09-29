import { getFormattedTime } from './utils.js'
import { updateFirstLap } from './laps.js'

const timer = document.getElementById('timer')

export let elapsedTime = 0
export let isRunning = false
let startTime = 0
let cancellationToken = undefined

function updateTimer(elapsedTime) {
  timer.innerText = getFormattedTime(elapsedTime)
}

function runTimer() {
  elapsedTime = Date.now() - startTime
  updateTimer(elapsedTime)
  updateFirstLap(elapsedTime)
  cancellationToken = setTimeout(runTimer, 1000 / 60) // 60 fps (frames per second)
}

export function startTimer() {
  if (!isRunning) {
    isRunning = true
    startTime += Date.now()
    runTimer()
  }
}

export function stopTimer() {
  if (isRunning) {
    isRunning = false
    startTime -= Date.now()
    clearTimeout(cancellationToken)
  }
}

export function resetTimer() {
  timer.innerText = '00:00.00'
  startTime = 0
  elapsedTime = 0
}
