export function zeroPad(number, places) {
  return number.toString().padStart(places, "0");
}

export function formatTime(milliseconds) {
  const date = new Date(milliseconds);
  const mins = date.getMinutes();
  const secs = date.getSeconds();
  const centis = Math.floor(date.getMilliseconds() / 10);

  return `${zeroPad(mins, 2)}:${zeroPad(secs, 2)}.${zeroPad(centis, 2)}`;
}