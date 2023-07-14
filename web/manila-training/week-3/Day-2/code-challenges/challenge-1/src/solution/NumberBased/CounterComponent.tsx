import React, { useEffect, useState } from 'react';
import { Subject, Observable } from 'rxjs';
import { map, mergeWith } from 'rxjs/operators';

// Create Subjects for actions
const increment$ = new Subject<number>();
const decrement$ = new Subject<number>();
const reset$ = new Subject<number>();

  // Merge all the actions into a single stream
  // take the value inputted into the next function and return it as a stream
const count$: Observable<number> =
  increment$.pipe(
    // incrementValue = 1
    map((incrementValue: number) => incrementValue),
    mergeWith(
      // decrementValue = -1
        decrement$.pipe(map((decrementValue: number) => decrementValue)),
      // resetValue = 0
        reset$.pipe(map((resetValue: number) => resetValue))
    )
  );

const CounterComponent: React.FC = () => {
  const [count, setCount] = useState(0);

  useEffect(() => {

    // Subscribe to the count and update state
    const subscription = count$.subscribe({
       next: (value: number) => {
        // handle the logic for what to do with the action values
        // and save the new value in state
        setCount((currentCount: number) => {
            if (value === 0) return 0; // reset
            return currentCount + value;
      });
    }});

    // Clean up the subscription
    return () => subscription.unsubscribe();
  }, []);

  return (
    <div>
      <p>{count}</p>
      <button onClick={() => increment$.next(1)}>Increment</button>
      <button onClick={() => decrement$.next(-1)}>Decrement</button>
      <button onClick={() => reset$.next(0)}>Reset</button>
    </div>
  );
};

export default CounterComponent;
