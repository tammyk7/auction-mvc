import React from 'react';
import { useValidation } from './useValidation';

// Use this as RegExp pattern for email ---> /^[^\s@]+@[^\s@]+\.[^\s@]+$/

type FormInputs = {
  // TODO
};

export const MyForm: React.FC = () => {
  return (
    <form onSubmit={() => window.alert('form submitted')}>
      <div>
        <label htmlFor="name">Name</label>
        <input
          type="text"
          name="name"
          data-testid="name-input"
        />
        {/** DISPLAY ERROR HERE */}
      </div>
      <div>
        <label htmlFor="email">Email</label>
        <input
          type="email"
          name="email"
          data-testid="email-input"
        />
        {/** DISPLAY ERROR HERE */}
      </div>
      <button type="submit" data-testid="submit-button">Submit</button>
    </form>
  );
};