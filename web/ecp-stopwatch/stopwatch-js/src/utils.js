const padNumber =  (number) => Math.floor(number).toString().padStart(2, '0')

export function getFormattedTime(milliseconds) {
  const totalSeconds = milliseconds / 1000

  const minutes = totalSeconds / 60
  const seconds = totalSeconds % 60
  const centis = milliseconds % 1000 / 10

  return `${padNumber(minutes)}:${padNumber(seconds)}.${padNumber(centis)}`
}

export function getFormattedTimeWithMap(milliseconds) {
    const totalSeconds = milliseconds / 1000

    const [minutes, seconds, centis] = [
        totalSeconds / 60,
        totalSeconds % 60,
        milliseconds % 1000 / 10
    ].map(padNumber)
  
    return `${minutes}:${seconds}.${centis}`
}

export function getFormattedTimeWithDate(milliseconds) {
    const date = new Date(milliseconds)

    const minutes = date.getMinutes()
    const seconds = date.getSeconds()
    const centis = date.getMilliseconds() / 10
  
    return `${padNumber(minutes)}:${padNumber(seconds)}.${padNumber(centis)}`
}