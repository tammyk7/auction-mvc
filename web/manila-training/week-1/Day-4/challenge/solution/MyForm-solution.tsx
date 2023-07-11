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
    (formInputs: FormInputs) => window.alert(`name: ${formInputs.name}, email: ${formInputs.email}`)
  );

  return (
    <form onSubmit={handleSubmit}>
      <div>
        <label htmlFor="name">Name</label>
        <input
          type="text"
          name="name"
          value={inputs.name}
          onChange={handleInputChange}
        />
        {errors.name && <span>{errors.name}</span>}
      </div>
      <div>
        <label htmlFor="email">Email</label>
        <input
          type="email"
          name="email"
          value={inputs.email}
          onChange={handleInputChange}
        />
        {errors.email && <span>{errors.email}</span>}
      </div>
      <button type="submit">Submit</button>
    </form>
  );
};