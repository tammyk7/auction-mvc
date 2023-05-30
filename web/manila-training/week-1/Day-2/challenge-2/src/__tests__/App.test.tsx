import { render, fireEvent, screen, act } from '@testing-library/react';
import CountdownTimer from '../App';
import { vi } from 'vitest';

describe('CountdownTimer', () => {
  test('should start and stop the timer correctly', () => {
    render(<CountdownTimer />);
    const input = screen.getByRole('textbox');
    const startButton = screen.getByRole('button', { name: 'Start' });
    const stopButton = screen.getByRole('button', { name: 'Stop' });

    // Start the timer
    fireEvent.change(input, { target: { value: '5' } });
    fireEvent.click(startButton);
    expect(screen.getByText('Time Remaining: 5 seconds')).toBeInTheDocument();

    // Stop the timer
    fireEvent.click(stopButton);
    expect(screen.getByText('Enter a duration and start the timer.')).toBeInTheDocument();

    // Advance time by 3 seconds (should not affect the stopped timer)
    act(() => {
      vi.useFakeTimers();
      jest.advanceTimersByTime(3000);
    });
    expect(screen.getByText('Enter a duration and start the timer.')).toBeInTheDocument();
  });

  test('should display "Timer Ended!" when the timer reaches zero', () => {
    render(<CountdownTimer />);
    const input = screen.getByRole('textbox');
    const startButton = screen.getByRole('button', { name: 'Start' });

    // Start the timer with 2 seconds duration
    fireEvent.change(input, { target: { value: '2' } });
    fireEvent.click(startButton);
    expect(screen.getByText('Time Remaining: 2 seconds')).toBeInTheDocument();

    // Advance time by 3 seconds
    act(() => {
      vi.useFakeTimers();
      jest.advanceTimersByTime(2000);
    });
    expect(screen.getByText('Timer Ended!')).toBeInTheDocument();
  });
});