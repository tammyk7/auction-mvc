export const getIdFromSWApiURL = (url: string) => {
  return `${+url.replace(/\D+/g, '')}`
}
