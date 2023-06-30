export const formatTime = (timeInSeconds: number) => {
  const [minutes, seconds] = [timeInSeconds / 60, timeInSeconds % 60].map(
    (num) => Math.floor(num).toString(10)
  )

  return `${minutes}m ${seconds}s`
}
