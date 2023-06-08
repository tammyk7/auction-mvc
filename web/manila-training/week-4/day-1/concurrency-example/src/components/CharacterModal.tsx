import { FC } from 'react'

// Import all package tools, components, and functions needed.

// Add the code needed to access the selected character,
// invoke the `fetchCharacter` function,
// get access to the observable returned by `fetchCharatcher`,
// and use that data within the component.

const CharacterModal: FC = (): JSX.Element => {
  return (
    <>
      <h2>{name}</h2>
      <p>
        <strong>Born: </strong>
        {birth_year}
      </p>
      <p>
        <strong>Height: </strong> {height} cm
      </p>
      <p>
        <strong>Hair Color: </strong> {hair_color}
      </p>
      <p>
        <strong>Eye Color: </strong> {eye_color}
      </p>
      <div className="closeModal" onClick={() => setSelectedCharacterId('')}>
        Close
      </div>
    </>
  )
}

export default CharacterModal
