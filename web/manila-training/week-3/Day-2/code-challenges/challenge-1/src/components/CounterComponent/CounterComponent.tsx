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