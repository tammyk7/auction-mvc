import React, { useRef, useReducer, useEffect } from 'react';
import "./App.css";

interface State {
  duration: number;
  remainingTime: number;
  isRunning: boolean;
}

const initialState: State = {
  duration: 0,
  remainingTime: 0,
  isRunning: false,
};

const reducer = (state: State, action: Action): State => {
  // TODO
};

const CountdownTimer: React.FC = () => {
  const [state, dispatch] = useReducer(reducer, initialState);

  useEffect(() => {
    // TODO
  }, [state.isRunning, state.remainingTime]);

  const startTimer = (): void => {
    // TODO
    alert("Start Timer");
  };
  
  const stopTimer = (): void => {
    // TODO
    alert("Stop Timer");
  };

  return (
    <div className="wrapper">
      <h1>Countdown Timer</h1>
      <input type="number" data-testid="timer-input" />
      <div>
        <button onClick={startTimer}>Start</button>
        <button onClick={stopTimer}>Stop</button>
      </div>
      {state.isRunning ? (
        <p>Time Remaining: {state.remainingTime} seconds</p>
      ) : (
        <p>Enter a duration and start the timer.</p>
      )}
      {state.remainingTime === 0 && <p>Timer Ended!</p>}
    </div>
  );
};

export default CountdownTimer;