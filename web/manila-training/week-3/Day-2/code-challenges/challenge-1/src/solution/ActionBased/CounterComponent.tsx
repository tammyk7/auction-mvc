
import React, { useEffect, useState } from 'react';
import { Observable, Subject, mergeWith } from 'rxjs';
import { map, scan } from 'rxjs/operators';

// Define action types
enum CounterActionType {
  Increment,
  Decrement,
  Reset
}

// Define actions
const increment =  ({ type: CounterActionType.Increment });
const decrement =  ({ type: CounterActionType.Decrement });
const reset =  ({ type: CounterActionType.Reset });

// Create Subjects for actions
const increment$ = new Subject<void>();
const decrement$ = new Subject<void>();
const reset$ = new Subject<void>();

const CounterComponent: React.FC = () => {
  const [count, setCount] = useState(0);

  useEffect(() => {
    // Create a stream of actions
    const actions$ =
      increment$.pipe(
        map(() => increment),
        mergeWith(
          decrement$.pipe(map(() => decrement)),
          reset$.pipe(map(() => reset))
        ))



    // Reduce actions to a count
    const count$: Observable<number> = actions$.pipe(
      scan((count: number, action: { type: CounterActionType}) => {
        switch (action?.type) {
          case CounterActionType.Increment:
            return count + 1;
          case CounterActionType.Decrement:
            return count - 1;
          case CounterActionType.Reset:
            return 0;
          default:
            return count;
        }
      }, 0) // 0 is initial value
    );

    // Subscribe to the count and update state
    const subscription = count$.subscribe({
      next: (count: number) => setCount(count)
    })

    // Clean up the subscription
    return () => subscription.unsubscribe();
  }, []);

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
