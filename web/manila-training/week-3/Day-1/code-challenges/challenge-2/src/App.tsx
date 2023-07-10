import React from 'react'
import UserProfileComponent from './components/UserProfileComponent/UserProfileComponent'
import { Subscribe } from '@react-rxjs/core';



function App() {
  return (
    <Subscribe>
      <UserProfileComponent username='limerentfox' />
    </Subscribe>

  )
}

export default App
