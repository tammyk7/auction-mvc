import { FC } from 'react'

const CharacterPage: FC = (): JSX.Element => {
  return (
    <>
      <nav>
        {/* Links back to character list page */}
        <a>&#8592;</a>
      </nav>
      <div>
        {/* Add image element */}
        <h3>Name</h3>
        <h4>Title</h4>
        <h4>Family</h4>
      </div>
    </>
  )
}

export default CharacterPage
