import { Routes, Route } from 'react-router-dom'

import './App.css'
import { Home, Post } from './components'

function App() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/post/:postId" element={<Post />} />
    </Routes>
  )
}

export default App
