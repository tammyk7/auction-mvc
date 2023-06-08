import { FC } from 'react'

import { getIdFromSWApiURL } from '../utils/getIdFromSWApiURL'

// Import all package tools, components, and functions needed.

// Add the code needed to access the character list returned from the `fetchCharacters` function.

const CharacterList: FC = (): JSX.Element => {
  const characters = ''

  return (
    <div className="list">
      {characters.results.map((character) => {
        return (
          <h3
            onClick={() =>
              setSelectedCharacterId(getIdFromSWApiURL(character.url))
            }
            key={character.name}
          >
            {character.name}
          </h3>
        )
      })}
    </div>
  )
}

export default CharacterList
