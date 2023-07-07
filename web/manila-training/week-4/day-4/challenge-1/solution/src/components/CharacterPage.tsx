import React, { FC } from 'react'
import { Link, useParams } from 'react-router-dom'

import { useCharacterData } from '../utils/useCharacterData'
import { Character } from '../services/thronesApi'

const CharacterPage: FC<{ characters: Character[] }> = ({
  characters,
}): JSX.Element => {
  const { id } = useParams()

  if (
    id === undefined ||
    !characters.find((character) => character.id === parseInt(id))
  ) {
    throw new Error('Character ID does not exist')
  }

  const characterData = useCharacterData(parseInt(id))

  return (
    <>
      <nav>
        <Link to="/">&#8592;</Link>
      </nav>
      <div>
        <img src={characterData.imageUrl} />
        <h3>{characterData.fullName}</h3>
        <h4>{characterData.title}</h4>
        <h4>{characterData.family}</h4>
      </div>
    </>
  )
}

export default CharacterPage
