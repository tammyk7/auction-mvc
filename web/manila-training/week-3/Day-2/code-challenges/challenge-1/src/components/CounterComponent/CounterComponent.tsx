
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