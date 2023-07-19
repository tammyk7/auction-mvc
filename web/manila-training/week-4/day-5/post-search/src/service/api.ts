const fetchData = async <T>(endpoint: string) => {
  const url = `https://jsonplaceholder.typicode.com/${endpoint}`
  const response = await fetch(url)
  const data = (await response.json()) as Promise<T>

  return data
}

export const fetchPost = (postId: string) => {
  return fetchData<PostItem>(`posts/${postId}`)
}

import { ajax } from 'rxjs/ajax'
import { Observable } from 'rxjs'

const fetchData$ = <T>(endpoint: string): Observable<T> => {
  const url = `https://jsonplaceholder.typicode.com/${endpoint}`
  return ajax.getJSON<T>(url)
}

export const fetchPost$ = (postId: string): Observable<PostItem> => {
  return fetchData$<PostItem>(`posts/${postId}`)
}

export interface PostItem {
  userId: number
  id: number
  title: string
  body: string
}
