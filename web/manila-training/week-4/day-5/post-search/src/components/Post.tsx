import { FC } from 'react'
import { Link, useParams } from 'react-router-dom'

import { usePostData } from '../utils'

const Post: FC = (): JSX.Element => {
  const { postId } = useParams()
  const post = usePostData(postId!)

  console.log(post)
  return (
    <div>
      <h1>Post {postId}</h1>
      {!post ? (
        <p>Loading...</p>
      ) : (
        <>
          <h2>{post.title}</h2>
          <p>{post.body}</p>
        </>
      )}
      <Link to="/">Back to Home</Link>
    </div>
  )
}

export default Post
