### Challenge 1: Counter Component

Create a Counter component using `react-rxjs`. The counter should have increment, decrement, and reset buttons. Use Signals to emit values when the buttons are clicked, and use `bind` to create a custom hook that subscribes to the Signals and updates the counter value.

Here's a skeleton to get you started:

```jsx
import React from 'react';
import { bind } from '@react-rxjs/core';
import { createSignal } from '@react-rxjs/utils';
import { merge } from 'rxjs';
import { scan } from 'rxjs/operators';

const [increment$, onIncrement] = createSignal();
const [decrement$, onDecrement] = createSignal();
const [reset$, onReset] = createSignal();

// TODO: define increment, decrement, and reset functions
// TODO: merge signals into a signal stream to handle counter logic
// TODO: create useCounter hook using bind
// TODO: add TS types for signals, components, and where necessary

function CounterComponent() {
  // TODO: Use useCounter hook to get current count
  return (
    <div>
      {/* TODO: add the current count to the UI */}
      {/* TODO: add buttons for incrementing, decrementing, and resetting the counter */}
    </div>
  );
}

export default CounterComponent;
```

To run this application:
1. `cd challenge-1`
2. `npm i`
3. `npm run dev`


Validate your solution by running it against the test suite:
`npm run test`
