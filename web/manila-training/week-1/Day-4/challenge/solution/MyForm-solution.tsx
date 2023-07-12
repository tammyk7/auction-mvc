import React from 'react';
import { useValidation } from './useValidation-solution';

type FormInputs = {
  name: string;
  email: string;
};

export const MyForm: React.FC = () => {
  const {
    inputs,
    errors,
    handleInputChange,
    handleSubmit
  } = useValidation<FormInputs>({
    name: {
      required: true,
    },
    email: {
      required: true,
      pattern: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
    },
  },
    (formInputs: FormInputs) => console.log(`name: ${formInputs.name}, email: ${formInputs.email}`)
  );

  return (
    <form onSubmit={handleSubmit}>
      <div>
        <label htmlFor="name">Name</label>
        <input
          type="text"
          name="name"
          data-testid="name-input"
          value={inputs.name || ''}
          onChange={handleInputChange}
        />
        {errors.name && <span data-testid="name-error">{errors.name}</span>}
      </div>
      <div>
        <label htmlFor="email">Email</label>
        <input
          type="email"
          name="email"
          data-testid="email-input"
          value={inputs.email || ''}
          onChange={handleInputChange}
        />
        {errors.email && <span data-testid="email-error">{errors.email}</span>}
      </div>
      <button type="submit" data-testid="submit-button">Submit</button>
    </form>
  );
};