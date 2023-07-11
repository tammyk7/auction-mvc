# Week 2 Day 5: React + RxJS Stopwatch Code Challenge

Create a Stopwatch application using RxJS and React that counts seconds but displays minutes & seconds.

When the start button is clicked the component should subscribe to an Observable that emits every second incrementing the time, but should unsubscribe when the user clicks stop.

You should also use RxJS to handle the clicking of the start and stop button.

We've included starter code for the component, a formatting function, and styles in the application.

Here's the starter code, located in `rxjs-timer/src/App.tsx`, to get you started:

```tsx
import { useState } from 'react'

import './App.css'
import { formatTime } from './utils/formatTime'

function App() {
  const [time, setTime] = useState(0)
  const [isRunning, setIsRunning] = useState(false)

  return (
    <>
      <h1>{formatTime(time)}</h1>
      <div>
        <button>Start</button>
        <button>Stop</button>
      </div>
    </>
  )
}

export default App
```

**NOTE:** Keep in mind this does not have to be a perfectly accurate stopwatch, the point of this challenge is to get you integrating RxJS in a React application.

To run this application:

1. `cd rxjs-timer`
2. `npm i`
3. `npm run dev`

After completing, take a look at `rxjs-timer/src/solution/App.tsx` to see our solution for this challenge.
