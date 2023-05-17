
import React, { FC } from 'react';
import { bind } from '@react-rxjs/core';
import { createSignal } from '@react-rxjs/utils';
import { merge } from 'rxjs';
import { scan } from 'rxjs/operators';

type CounterProps = {};

const [increment$, onIncrement] = createSignal();
const [decrement$, onDecrement] = createSignal();
const [reset$, onReset] = createSignal();

// TODO: Define increment, decrement, and reset functions
// TODO: Create useCounter hook using bind

const CounterComponent: FC<CounterProps> = (): JSX.Element => {
  // TODO: Use useCounter hook to get current count
  return (
    <div>
      {/* TODO: Add buttons for incrementing, decrementing, and resetting the counter */}
    </div>
  );
}


