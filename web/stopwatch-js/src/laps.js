import { getFormattedTime } from './utils.js'

const lapsTable = document.getElementById('lapsTable')

let lapData = {
  totalLapTime: 0,
  numberLaps: 0,
  minLap: { row: undefined, time: Infinity },
  maxLap: { row: undefined, time: 0 },
}

export function updateFirstLap(elapsedTime) {
  lapsTable.rows[0].cells[1].innerText = getFormattedTime(elapsedTime - lapData.totalLapTime)
}

export function createFirstLap() {
  const lapRow = lapsTable.insertRow(0)
  lapRow.insertCell(0).innerText = `Lap ${lapData.numberLaps + 1}`
  lapRow.insertCell(1).innerText = `00:00.00`
}

export function addLap(elapsedTime) {
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

export function resetLaps() {
  lapsTable.innerHTML = ''
  lapData = {
    totalLapTime: 0,
    numberLaps: 0,
    minLap: { row: undefined, time: Infinity },
    maxLap: { row: undefined, time: 0 },
  }
}
