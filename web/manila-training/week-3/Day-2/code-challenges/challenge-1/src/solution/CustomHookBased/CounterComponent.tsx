import React, { useEffect, useState } from 'react';
import { Observable, Subject } from 'rxjs';
import { map, mergeWith, scan } from 'rxjs/operators';

// Create Subjects for actions
const increment$ = new Subject<void>();
const decrement$ = new Subject<void>();
const reset$ = new Subject<void>();
// ^^^ These subjects don't need to take values as we are creating
// streams that simply return a set number value below



// Merge all the actions into a single stream
 const count$: Observable<number> =
      increment$.pipe(
        map(() => 1),
        mergeWith(
            decrement$.pipe(map(() => -1)),
            reset$.pipe(map(() => 0))
        ),
        // scan behaves exactly like Array.reduce
        // this is where we handle the logic of what to do
        // with the numbers returned by the stream
        scan((totalCount: number, change: number) => change === 0 ? 0 : totalCount + change, 0) //  0 is initial value
    );

// Create a custom hook to gain access to the count
const useCounter: () => number = () => {

  const [count, setCount] = useState(0);

  useEffect(() => {
    // Subscribe to the count and update state
    const subscription = count$.subscribe({
       next: (value: number) => {
        setCount(value)
    }});

    // Clean up the subscription
    return () => subscription.unsubscribe();
  }, []);

  return count;
};

const CounterComponent: React.FC = () => {
  const count: number = useCounter();

  return (
    <div>
      <p>{count}</p>
      <button onClick={() => increment$.next()}>Increment</button>
      <button onClick={() => decrement$.next()}>Decrement</button>
      <button onClick={() => reset$.next()}>Reset</button>
    </div>
  );
};

export default CounterComponent;
