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