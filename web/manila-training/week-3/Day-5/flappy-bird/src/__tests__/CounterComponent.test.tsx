import React from 'react';
import { render, fireEvent } from '@testing-library/react';
import CounterComponent from '../components/CounterComponent/CounterComponent';

describe('CounterComponent', () => {
  it('should increment the count when the increment button is clicked', () => {
    const { getByText } = render(<CounterComponent />);
    fireEvent.click(getByText('Increment'));
    expect(getByText('Count: 1')).toBeInTheDocument();
  });

  it('should decrement the count when the decrement button is clicked', () => {
    const { getByText } = render(<CounterComponent />);
    fireEvent.click(getByText('Decrement'));
    expect(getByText('Count: -1')).toBeInTheDocument();
  });

  it('should reset the count when the reset button is clicked', () => {
    const { getByText } = render(<CounterComponent />);
    fireEvent.click(getByText('Reset'));
    expect(getByText('Count: 0')).toBeInTheDocument();
  });
});

