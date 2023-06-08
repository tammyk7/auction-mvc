### Challenge 1: Counter Component without React-RxJS

Create a Counter component using React, TypeScript, and RxJS. The counter should have increment, decrement, and reset buttons. Use Subjects to emit values when the buttons are clicked, and use Observables to subscribe to the Subjects and update the counter value.

Here's a skeleton to get you started:

```jsx
import React, { useEffect, useState } from 'react';
import { Subject } from 'rxjs';
import { scan } from 'rxjs/operators';

const increment$ = new Subject();
const decrement$ = new Subject();
const reset$ = new Subject();

// TODO: Define increment, decrement, and reset functions
// TODO: Create useCounter hook using Observables

function CounterComponent() {
  // TODO: Use useCounter hook to get current count
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
