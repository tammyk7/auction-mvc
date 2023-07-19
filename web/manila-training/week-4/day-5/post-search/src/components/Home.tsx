import { FC } from 'react'
import { bind } from '@react-rxjs/core'
import { useNavigate } from 'react-router-dom'
import { createSignal } from '@react-rxjs/utils'

const [postId$, setPostId] = createSignal<string>()

const [usePostId] = bind(postId$, '')

const Home: FC = (): JSX.Element => {
  const [postId] = usePostId()
  const navigate = useNavigate()

  return (
    <div>
      <h1>Welcome!</h1>
      <h2>Here you can search for a post. The only thing you need is its ID</h2>

      <label htmlFor="postId">Post ID: </label>
      <input id="postId" onChange={(e) => setPostId(e.target.value)} />
      <button disabled={!postId} onClick={() => navigate(`/post/${postId}`)}>
        Submit
      </button>
    </div>
  )
}

export default Home
