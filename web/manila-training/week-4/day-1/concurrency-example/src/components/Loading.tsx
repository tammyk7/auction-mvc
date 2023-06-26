import { FC } from 'react'

type LoadingProps = { message?: string }

const Loading: FC<LoadingProps> = ({ message }): JSX.Element => {
  return (
    <div className="loading">
      {message ? <h3>Loading {message}...</h3> : <h1>Loading...</h1>}
    </div>
  )
}

export default Loading
