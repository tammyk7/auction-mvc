import { getFormattedTime } from './utils.js'

const timer = document.getElementById('timer')
const startStopButton = document.getElementById('startStopButton')
const lapResetButton = document.getElementById('lapResetButton')
const lapsTable = document.getElementById('lapsTable')

let isTimerRunning = false
let startTime = 0
let elapsedTime = 0
let timerId = undefined
let lapData = {
  totalLapTime: 0,
  numberLaps: 0,
  minLap: { row: undefined, time: Infinity },
  maxLap: { row: undefined, time: 0 },
}

function updateTimer(elapsedTime) {
  timer.innerText = getFormattedTime(elapsedTime)
}

function runTimer() {
  elapsedTime = Date.now() - startTime
  updateTimer(elapsedTime)
  updateFirstLap(elapsedTime)
  timerId = setTimeout(runTimer, 50)
}

function startTimer() {
  if (!isTimerRunning) {
    isTimerRunning = true
    startTime += Date.now()
    if (elapsedTime === 0) {
      createFirstLap()
    }
    runTimer()
  }
}

function stopTimer() {
  if (isTimerRunning) {
    isTimerRunning = false
    startTime -= Date.now()
    clearTimeout(timerId)
  }
}

function resetTimer() {
  timer.innerText = '00:00.00'
  startTime = 0
  elapsedTime = 0
}

function createFirstLap() {
  const lapRow = lapsTable.insertRow(0)
  lapRow.insertCell(0).innerText = `Lap ${lapData.numberLaps + 1}`
  lapRow.insertCell(1).innerText = `00:00.00`
}

function updateFirstLap(elapsedTime) {
  lapsTable.rows[0].cells[1].innerText = getFormattedTime(elapsedTime - lapData.totalLapTime)
}

function addLap(elapsedTime) {
  const newLapTime = elapsedTime - lapData.totalLapTime
  lapData.totalLapTime += newLapTime
  lapData.numberLaps += 1

  // update first lap
  lapsTable.rows[0].cells[0].innerText = `Lap ${lapData.numberLaps + 1}`
  lapsTable.rows[0].cells[1].innerText = `00:00.00`

  // insert new lap
  const lapRow = lapsTable.insertRow(1)
  lapRow.insertCell(0).innerText = `Lap ${lapData.numberLaps}`
  lapRow.insertCell(1).innerText = getFormattedTime(newLapTime)

  // update max and min lap
  if (newLapTime > lapData.maxLap.time) {
    lapData.maxLap.row?.classList.remove('max-lap')
    lapData.maxLap = { row: lapRow, time: newLapTime }
  }

  if (newLapTime < lapData.minLap.time) {
    lapData.minLap.row?.classList.remove('min-lap')
    lapData.minLap = { row: lapRow, time: newLapTime }
  }

  if (lapData.numberLaps >= 2) {
    lapData.maxLap.row.classList.add('max-lap')
    lapData.minLap.row.classList.add('min-lap')
  }
}

function resetLaps() {
  lapsTable.innerHTML = ''
  lapData = {
    totalLapTime: 0,
    numberLaps: 0,
    minLap: { row: undefined, time: Infinity },
    maxLap: { row: undefined, time: 0 },
  }
}

startStopButton.onclick = () => {
  if (!isTimerRunning) {
    startTimer(timer)
    startStopButton.innerText = 'Stop'
    startStopButton.className = 'started'
    lapResetButton.disabled = false
    lapResetButton.innerText = 'Lap'
  } else {
    stopTimer()
    startStopButton.innerText = 'Start'
    startStopButton.className = 'stopped'
    lapResetButton.innerText = 'Reset'
  }
}

lapResetButton.onclick = () => {
  if (isTimerRunning) {
    addLap(elapsedTime)
  } else {
    resetTimer()
    resetLaps()
    startStopButton.innerText = 'Start'
    lapResetButton.innerText = 'Lap'
    lapResetButton.disabled = true
  }
}
