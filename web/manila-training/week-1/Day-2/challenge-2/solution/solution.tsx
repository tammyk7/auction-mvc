import React, { useRef, useReducer, useEffect } from 'react';
import "./App.css";

interface State {
  duration: number;
  remainingTime: number;
  isRunning: boolean;
}

type Action =
  | { type: 'SET_DURATION'; duration: number }
  | { type: 'START_TIMER' }
  | { type: 'TICK' }
  | { type: 'STOP_TIMER' };

const initialState: State = {
  duration: 0,
  remainingTime: 0,
  isRunning: false,
};

const reducer = (state: State, action: Action): State => {
  switch (action.type) {
    case 'SET_DURATION':
      return {
        ...state,
        duration: action.duration,
        remainingTime: action.duration,
      };
    case 'START_TIMER':
      return {
        ...state,
        isRunning: true,
      };
    case 'TICK':
      return {
        ...state,
        remainingTime: state.remainingTime - 1,
      };
    case 'STOP_TIMER':
      return {
        ...state,
        isRunning: false,
      };
    default:
      return state;
  }
};

const CountdownTimer: React.FC = () => {
  const inputRef = useRef<HTMLInputElement>(null);
  const intervalRef = useRef<number | undefined>(undefined);
  const [state, dispatch] = useReducer(reducer, initialState);

  useEffect(() => {
    if (state.isRunning) {
      intervalRef.current = window.setInterval(() => {
        dispatch({ type: 'TICK' });
      }, 1000);

      if (state.remainingTime === 0) {
        dispatch({ type: 'STOP_TIMER' });
      }
    } else {
      clearInterval(intervalRef.current);
    }

    return () => {
      clearInterval(intervalRef.current);
    };
  }, [state.isRunning, state.remainingTime]);

  const startTimer = (): void => {
    const duration = parseInt(inputRef.current?.value || '');
    if (duration > 0) {
      dispatch({ type: 'SET_DURATION', duration });
      dispatch({ type: 'START_TIMER' });
    }
  };

  const stopTimer = (): void => {
    dispatch({ type: 'STOP_TIMER' });
  };

  return (
    <div className="wrapper">
      <h1>Countdown Timer</h1>
      <input type="number" ref={inputRef} data-testid="timer-input" />
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