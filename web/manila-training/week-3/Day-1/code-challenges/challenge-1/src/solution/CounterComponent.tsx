import React from 'react';
import { bind } from '@react-rxjs/core';
import { createSignal } from '@react-rxjs/utils';
import { Observable, merge } from 'rxjs';
import { scan } from 'rxjs/operators';

const [increment$, onIncrement]: [Observable<number>, (value: number) => void] = createSignal<number>();
const [decrement$, onDecrement]: [Observable<number>, (value: number) => void] = createSignal<number>();
const [reset$, onReset]: [Observable<number>, (value: number) => void] = createSignal<number>();

// Define increment, decrement, and reset functions
const increment: () => void = () => onIncrement(1);
const decrement: () => void = () => onDecrement(-1);
const reset: () => void = () => onReset(0);

// Merge streams into one
const count$: Observable<number> = merge(
  increment$,
  decrement$,
  reset$
).pipe(scan((count: number, change: number) => {
    console.log({change, count})
    return change !== 0 ? count + change : 0
}, 0));

// This code creates an observable stream count$ which emits numbers. It combines three other observables (increment$, decrement$, and reset$) using the merge operator.

// The scan operator is then applied to the merged observable. The scan operator accumulates values over time by applying a function to each emitted value and the accumulation result so far. In this case, the function takes two parameters: count (the current accumulated count) and change (the emitted value from the merged observables).

// Inside the scan callback function, there is a console log statement that logs the current change and count values. Then, it checks if change is not equal to zero. If it's not zero, it adds the change value to the count; otherwise, it resets the count to zero. The resulting value is emitted as the new accumulated count.

// The initial accumulated count is set to 0

// Overall, this code sets up an observable stream that keeps track of a count by merging three other observables and updating the count based on their emitted values.


// ===================================================

// Create hook useCounter to get the current count
const [useCounter] = bind(count$, 0);

const CounterComponent: React.FC = () => {
  const count = useCounter();
  return (
    <div>
      Count: {count}
      <button onClick={increment}>Increment</button>
      <button onClick={decrement}>Decrement</button>
      <button onClick={reset}>Reset</button>
    </div>
  );
}

export default CounterComponent;
