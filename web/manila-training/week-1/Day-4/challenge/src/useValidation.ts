import { useState } from 'react';

type ValidationSchema<T> = {
  // TODO
};

type ValidationErrors<T> = {
  // TODO
};

type UseValidation<T> = {
  inputs: T;
  errors: ValidationErrors<T>;
  handleInputChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
  handleSubmit: (event: React.FormEvent<HTMLFormElement>) => void;
};

export const useValidation = <T extends Record<string, any>>(
  validationSchema: ValidationSchema<T>,
  onSubmit?: (inputs: T) => void
): UseValidation<T> => {

  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    // TODO
  };

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    // TODO
  };

  return {
    // inputs,
    // errors,
    // handleInputChange,
    // handleSubmit,
  };
};