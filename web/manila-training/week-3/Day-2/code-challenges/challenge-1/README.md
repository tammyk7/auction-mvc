### Challenge 1: Counter Component without React-RxJS

Create a Counter component using React, TypeScript, and RxJS. The counter should have increment, decrement, and reset buttons. Use Subjects to emit values when the buttons are clicked, and use Observables to subscribe to the Subjects and update the counter value.

Here's a skeleton to get you started:

```jsx
import React, { useEffect, useState } from 'react';
import { Subject } from 'rxjs';
// TODO: Import necessary operators from rxjs

// TODO: Using the below 3 subjects to handle the different actions,
// figure out how to increment, decrement & reset a counter starting at 0

const increment$ = new Subject();
const decrement$ = new Subject();
const reset$ = new Subject();

// NOTE: The logic for the counter should be handled by rxjs

function CounterComponent() {
 const [count, setCount] = useState(0);

 useEffect(() => {
   // TODO: Handle subscribing & unsubscribing from stream(s)
 })


  return (
    <div>
      {/* TODO: Add buttons for incrementing, decrementing, and resetting the counter */}
    </div>
  );
}
```

To run this application:
1. `cd challenge-1`
2. `npm i`
3. `npm run dev`


Validate your solution by running it against the test suite:
`npm run test`
