import { FC } from 'react'

// import components and functions:
//    - Loading
//    - CharacterList
//    - CharacterModal
//    - useSelectedCharacterId

const Characters: FC = (): JSX.Element => {
  //
  const selectedCharacterId = ''

  return (
    <>
      <div className="page">
        <h1>Star Wars Characters:</h1>
        {/* Add the code to display the character list here with a fallback UI. */}
      </div>
      {selectedCharacterId && (
        <div className="modal">
          <div className="modal-content">
            {/* Add the code to display the character modal here with a fallback UI.  */}
          </div>
        </div>
      )}
    </>
  )
}

export default Characters
