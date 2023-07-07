import { FC } from 'react'

const CharacterList: FC = (): JSX.Element => {
  return (
    <>
      <h1>Game of Thrones Characters</h1>
      <div className="list">
        {/* Replace with character list that links to character detail page*/}
        <a>
          <h3>{'Sample Character'}</h3>
        </a>
      </div>
    </>
  )
}

export default CharacterList
