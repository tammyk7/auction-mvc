import { useState } from 'react';

type ValidationSchema<T> = {
  [K in keyof T]: {
    required?: boolean;
    pattern?: RegExp;
  };
};

type ValidationErrors<T> = {
  [K in keyof T]?: string;
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
  const [inputs, setInputs] = useState<T>({} as T);
  const [errors, setErrors] = useState<ValidationErrors<T>>({});

  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target;
    setInputs((prevInputs) => ({ ...prevInputs, [name]: value }));
  };

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const validationErrors: ValidationErrors<T> = {};

    for (const fieldName in validationSchema) {
      const fieldRules = validationSchema[fieldName as keyof T];

      if (fieldRules.required && !inputs[fieldName as keyof T]) {
        validationErrors[fieldName as keyof T] = 'This field is required';
      }

      if (fieldRules.pattern && inputs[fieldName as keyof T] && !fieldRules.pattern.test(inputs[fieldName as keyof T])) {
        validationErrors[fieldName as keyof T] = 'This field is invalid';
      }
    }

    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
    } else {
      // console.log('Inputs: ', inputs);
      onSubmit && onSubmit(inputs);
      setInputs({} as T);
      setErrors({});
    }
  };

  return {
    inputs,
    errors,
    handleInputChange,
    handleSubmit,
  };
};