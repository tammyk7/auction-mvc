import React, { FC } from 'react'
import { Link } from 'react-router-dom'
import { useCharacters } from '../services/useCharacters'

const CharacterList: FC = (): JSX.Element => {
  const characters = useCharacters()

  return (
    <>
      <h1>Game of Thrones Characters</h1>
      <div className="list">
        {characters.map((character) => {
          return (
            <Link to={`/character/${character.id}`} key={character.id}>
              <h3>{character.fullName}</h3>
            </Link>
          )
        })}
      </div>
    </>
  )
}

export default CharacterList
