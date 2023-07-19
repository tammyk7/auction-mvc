import { useEffect, useState } from 'react'
import { PostItem, fetchPost$ } from '../service/api'
import { of, switchMap } from 'rxjs'

export const usePostData = (id: string): PostItem | undefined => {
  const [returnData, setReturnData] = useState<PostItem>()

  useEffect(() => {
    const postData$ = of(id).pipe(switchMap((id) => fetchPost$(id)))

    const subscription = postData$.subscribe((val) => setReturnData(val))

    return () => subscription.unsubscribe()
  }, [id])

  return returnData
}
