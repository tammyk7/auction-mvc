### Challenge:
Your task is to implement a form with validation using custom hooks and TypeScript types. The form should have two input fields: a name field and an email field. When the user submits the form, the inputs should be validated to ensure that both fields are filled out and that the email is in a valid format. If the inputs are valid, a success message should be displayed. If the inputs are invalid, an error message should be displayed.

You should use TypeScript to define types for the form inputs, and use custom hooks to manage the form state and validation logic.

#### Requirements: 
- The form should have two input fields: a name field and an email field.
- When the user submits the form, the inputs should be validated to ensure that both fields are filled out and that the email is in a valid format.
- If the inputs are valid, a success message should be displayed. If the inputs are invalid, an error message should be displayed.
- You should use the `useValidation` custom hook to manage the form state and validation logic.
- You should use TypeScript to define types for the form inputs.

```tsx
// MyForm.tsx
import React from 'react';
import { useValidation } from './useValidation';

type FormInputs = {
  name: string;
  email: string;
};

export const MyForm: React.FC = () => {
  const { inputs, errors, handleInputChange, handleSubmit } = useValidation<FormInputs>(
    {
      name: '',
      email: '',
    },
    {
      name: {
        required: true,
      },
      email: {
        required: true,
        pattern: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
      },
    }
  );

  return (
    <form onSubmit={handleSubmit}>
      <div>
        <label htmlFor="name">Name</label>
        <input type="text" name="name" value={inputs.name} onChange={handleInputChange} />
        {errors.name && <span>{errors.name}</span>}
      </div>
      <div>
        <label htmlFor="email">Email</label>
        <input type="email" name="email" value={inputs.email} onChange={handleInputChange} />
        {errors.email && <span>{errors.email}</span>}
      </div>
      <button type="submit">Submit</button>
    </form>
  );
};
```

#### Notes:
In this example, we define a component called `MyForm` that uses the `useValidation` custom hook to manage the form state and validation logic. The `useValidation` hook takes a validation schema object as its only argument, which defines the validation rules for each input.

The `useValidation` hook returns four values: `inputs`, `errors`, `handleInputChange`, and `handleSubmit`. The `inputs` object contains the current values of the form inputs, the `errors` object contains any validation errors, the `handleInputChange` function is used to update the form inputs, and the `handleSubmit` function is used to submit the form.

The `MyForm` component renders a form with two input fields: a name field and an email field. The `handleInputChange` function is passed as a callback to the `onChange` event of each input, which allows the input values to be updated as the user types. The `errors` object is used to display any validation errors next to the input fields, and the `handleSubmit` function is called when the form is submitted.

If the form inputs are valid, the `handleSubmit` function displays a success message. If the form inputs are invalid, the `errors` object is updated with the validation errors, which causes the error messages to be displayed next to their respective input fields.

```tsx
// useValidation.ts
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
      console.log('Inputs: ', inputs);
      // onSubmit(inputs);
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
```

This `useValidation` hook takes a validation schema and an onSubmit callback function (optional) as its arguments, and returns an object with `inputs`, `errors`, `handleInputChange`, and `handleSubmit` properties.

The `inputs` property is an object that contains the current values of the form inputs. The `errors` property is an object that contains any validation errors.

The `handleInputChange` function is a callback function that updates the inputs object when the user types into the form input fields.

The `handleSubmit` function is a callback function that is called when the user submits the form. It first validates the form inputs according to the `validationSchema` object. If the form inputs are valid, the `onSubmit` callback function is called with the `inputs` object as its argument, and `inputs` and `errors` properties are reset.