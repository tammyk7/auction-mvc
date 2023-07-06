import React from 'react'
import CounterComponent from './components/CounterComponent/CounterComponent'
import './App.css'
import { Subscribe } from '@react-rxjs/core';

function App() {
  return (
    <Subscribe>
      <CounterComponent />
    </Subscribe>
  )
}

export default App
