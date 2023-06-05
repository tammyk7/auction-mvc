import React, { useEffect } from 'react';
import { bind } from '@react-rxjs/core';
import { fromEvent, interval, merge } from 'rxjs';
import { map, scan, filter, takeWhile, startWith, withLatestFrom } from 'rxjs/operators';

// Define the game state
interface GameState {
  birdPosition: number;
  obstaclePosition: number;
  gameOver: boolean;
}

// Constants
const BIRD_START_POSITION = 50;
const OBSTACLE_START_POSITION = 100;
const OBSTACLE_SPEED = -2;

// Create a signal for the bird's position
const [useBirdPosition, birdPosition$] = bind(
  merge(
    fromEvent(document, 'keydown').pipe(
      filter((event: KeyboardEvent) => event.code === 'Space'),
      map(() => -30),
    ),
    interval(20).pipe(map(() => 3)),
  ).pipe(
    startWith(BIRD_START_POSITION),
    scan((position, change) => Math.max(0, position + change)),
    takeWhile((position) => position < 500),
  ),
  BIRD_START_POSITION,
);

// Create a signal for the obstacle's position
const [useObstaclePosition, obstaclePosition$] = bind(
  interval(20).pipe(
    map(() => ({
      x: OBSTACLE_SPEED,
      y: Math.random() * 2 - 1, // Randomly move up or down
    })),
    startWith({ x: OBSTACLE_START_POSITION, y: OBSTACLE_START_POSITION }),
    scan((position, change) => ({
      x: (position.x + change.x < 0 ? OBSTACLE_START_POSITION : position.x + change.x),
      y: Math.max(0, Math.min(100, position.y + change.y)),
    })),
  ),
  { x: OBSTACLE_START_POSITION, y: OBSTACLE_START_POSITION },
);

// The main game component
const FlappyBird: React.FC = () => {
  const birdPosition = useBirdPosition();
  const obstaclePosition = useObstaclePosition();
  const gameOver = birdPosition >= 500;

  // Collision detection
  useEffect(() => {
    const collision$ = birdPosition$.pipe(
      withLatestFrom(obstaclePosition$),
      filter(([birdPosition, obstaclePosition]) => Math.abs(birdPosition - obstaclePosition.y) < 10 && obstaclePosition.x < 60),
    );

    const subscription = collision$.subscribe(() => {
      alert('Game Over!');
    });

    return () => {
      subscription.unsubscribe();
    };
  }, [birdPosition$, obstaclePosition$]);

  return (
    <div>
      <div
        style={{
          position: 'relative',
          height: '500px',
          width: '500px',
          border: '1px solid black',
        }}
      >
        <div
          style={{
            position: 'absolute',
            top: `${birdPosition}px`,
            left: '50px',
            height: '50px',
            width: '50px',
            backgroundColor: 'red',
          }}
        />
        <div
          style={{
            position: 'absolute',
            top: `${obstaclePosition.y}px`,
            left: `${obstaclePosition.x}px`,
            height: '50px',
            width: '50px',
            backgroundColor: 'green',
          }}
        />
      </div>
      {gameOver && <h1>Game Over</h1>}
    </div>
  );
};

export default FlappyBird;