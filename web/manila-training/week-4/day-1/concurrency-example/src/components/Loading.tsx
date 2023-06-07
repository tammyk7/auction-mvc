import { FC } from 'react'

type LoadingProps = { message?: string }

const Loading: FC<LoadingProps> = ({ message }): JSX.Element => {
  return (
    <div className="loading">
      <h3>Loading{message ? ` ${message}...` : '...'}</h3>
    </div>
  )
}

export default Loading
