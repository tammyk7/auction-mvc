import { of, switchMap } from 'rxjs'
import { useEffect, useState } from 'react'

import { Character, fetchCharacter } from '../services/thronesApi'

export const useCharacterData = (id: number): Character => {
  const [returnData, setReturnData] = useState<Character>({})

  useEffect(() => {
    const characterData$ = of(id).pipe(switchMap((id) => fetchCharacter(id)))

    const subscription = characterData$.subscribe((val) => setReturnData(val))

    return () => subscription.unsubscribe()
  }, [id])

  return returnData
}
