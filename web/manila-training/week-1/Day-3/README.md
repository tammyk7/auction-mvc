# Day 3: Custom Hooks and TypeScript interfaces

## Creating and Using Custom Hooks in React

Custom hooks are a powerful feature in React that allow developers to encapsulate and reuse logic across different components. They provide a way to extract common functionality into a separate function, making code more modular, reusable, and easier to maintain. In this write-up, we will explore the concept of custom hooks, walk through the process of creating a custom hook, and discuss the benefits and best practices associated with their usage.

### Understanding Custom Hooks

Custom hooks in React are JavaScript functions that follow a specific naming convention, starting with the prefix "use." They allow you to encapsulate reusable logic, including state, side effects, and other hooks, into a custom function that can be easily consumed by functional components.

Lets walk through the process of creating a custom hook called `useToggle` that manages a boolean state.

```jsx
import { useState } from 'react';

const useToggle = (initialState = false) => {
  const [value, setValue] = useState(initialState);

  const toggle = () => {
    setValue((prevValue) => !prevValue);
  };

  return [value, toggle] as const;
};
```

> :bulb: If you are returning an array in your Custom Hook, you will want to avoid type inference as TypeScript will infer a union type (when you actually want different types in each position of the array). Read more [here](https://react-typescript-cheatsheet.netlify.app/docs/basic/getting-started/hooks/#custom-hooks).


In this example, we define the custom hook `useToggle` using the existing `useState` hook. It accepts an optional initial state and returns an array with two elements: the current boolean value and a `toggle` function to update the value.

To use the `useToggle` custom hook in a functional component, simply call it and destructure the returned values:

```jsx
import React from 'react';
import useToggle from './useToggle';

const MyComponent = () => {
  const [isToggled, toggle] = useToggle(false);

  return (
    <div>
      <button onClick={toggle}>Toggle</button>
      {isToggled && <p>Toggle is ON</p>}
    </div>
  );
};
```

In this example, we use the `useToggle` hook to manage the state of a toggle button. By calling the `toggle` function, we can update the boolean value and render the corresponding content conditionally.

### Benefits of Custom Hooks:

Custom hooks offer several benefits in React development:

- **Code Reusability**: Custom hooks allow us to extract common logic into separate functions, making it easier to reuse the code across multiple components.

- **Abstraction and Modularity**: By encapsulating complex logic within custom hooks, components become more focused and easier to understand, promoting code maintainability.

- **Enhancing Readability**: Custom hooks improve code readability by abstracting away implementation details and providing a higher-level interface.

### Benefits of Custom Hooks

Custom hooks offer several benefits in React development:

- **Code Reusability**: Custom hooks allow us to extract common logic into separate functions, making it easier to reuse the code across multiple components.

- **Abstraction and Modularity**: By encapsulating complex logic within custom hooks, components become more focused and easier to understand, promoting code maintainability.

- **Enhancing Readability**: Custom hooks improve code readability by abstracting away implementation details and providing a higher-level interface.

### Best Practices for Creating Custom Hooks:

To create effective and reusable custom hooks, it's important to follow these best practices:

- **Name Convention**: Custom hooks should always start with the prefix "use" to indicate that they follow the rules of hooks and are intended to be used in functional components.

- **Decoupling**: Custom hooks should be self-contained and independent of any specific component or context, making them more versatile and reusable.

- **Clear Documentation**: Provide clear and concise documentation explaining the usage, purpose, and any additional parameters or return values of the custom hook.

### Additional Examples of Custom Hooks:

Custom hooks can be used for various use cases, such as handling form validation, managing API requests, or implementing infinite scrolling. Here are some additional examples:

- `useLocalStorage`: A hook that synchronizes state with the browser's localStorage.

- `useDebounce`: A hook that delays the execution of a function until a specified time has passed.

- `useMediaQuery`: A hook that tracks the current media query state, enabling responsive behavior.

Custom hooks are a powerful tool in React that allow developers to encapsulate and reuse logic in a modular and efficient manner. By creating custom hooks, you can abstract away complex implementation details and build reusable, readable, and maintainable code. Embrace the concept of custom hooks and explore their versatility to enhance your React applications.

---

## Reusing Logic Across Components with Custom Hooks

Reusing logic across multiple components is a common requirement in React applications. It allows us to avoid code duplication, improve code organization, and enhance development efficiency. In this write-up, we will explore the concept of reusing logic with custom hooks, discuss scenarios where custom hooks can be beneficial, walk through the process of creating a custom hook for form handling, and highlight the advantages and best practices associated with reusing logic through custom hooks.

Custom hooks provide an elegant solution for reusing logic across multiple components in React. They allow us to extract common functionality into a custom hook, encapsulating state, side effects, or any other hooks, which can then be easily consumed by different components. By centralizing and abstracting reusable logic, custom hooks enable us to build more modular and maintainable applications.

### Scenarios Where Custom Hooks Are Beneficial:

- **Form Validation**: Creating a custom hook that handles form validation logic, such as field validation, error handling, and form submission.

- **API Requests**: Building a custom hook that manages API requests, including data fetching, error handling, and loading state management.

- **Authentication**: Creating a custom hook that encapsulates authentication logic, such as login, logout, and session management.

### Creating a Custom Hook: `useForm` (for Form Handling)

Let's walk through the process of creating a custom hook called useForm that encapsulates form handling logic.

```jsx
import { useState } from 'react';

interface FormValues {
  username?: string
  email: string
  password: string
}

export const useForm = (
  initialValues: FormValues,
  onSubmit: (formValues: FormValues) => void
) => {
  const [values, setValues] = useState(initialValues);

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target;
    setValues((prevValues) => ({ ...prevValues, [name]: value }));
  };

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    onSubmit(values);
  };

  return { values, handleChange, handleSubmit };
};
```

In this example, the `useForm` custom hook takes in initial form values and a submit callback function. It returns an object with values representing the current form values, `handleChange` for handling input changes, and `handleSubmit` for submitting the form.

We can now reuse the form handling logic across multiple components:

```jsx
import React from 'react';
import { useForm } from './useForm';

// login.tsx
export const Login = () => {
  const { values, handleChange, handleSubmit } = useForm(
    { email: '', password: '' },
    (formData) => {
      // Perform login logic using formData
      console.log('Login form submitted:', formData);
    }
  );

  return (
    <form onSubmit={handleSubmit}>
      <input
        type="email"
        name="email"
        value={values.email}
        onChange={handleChange}
        placeholder="Email"
      />
      <input
        type="password"
        name="password"
        value={values.password}
        onChange={handleChange}
        placeholder="Password"
      />
      <button type="submit">Login</button>
    </form>
  );
};

// signup.tsx
export const Signup = () => {
  const { values, handleChange, handleSubmit } = useForm(
    { username: '', email: '', password: '' },
    (formData) => {
      // Perform signup logic using formData
      console.log('Signup form submitted:', formData);
    }
  );

  return (
    <form onSubmit={handleSubmit}>
      <input
        type="text"
        name="username"
        value={values.username}
        onChange={handleChange}
        placeholder="Username"
        required
      />
      <input
        type="email"
        name="email"
        value={values.email}
        onChange={handleChange}
        placeholder="Email"
      />
      <input
        type="password"
        name="password"
        value={values.password}
        onChange={handleChange}
        placeholder="Password"
      />
      <button type="submit">Signup</button>
    </form>
  );
};
```

In this example, we reuse the useForm custom hook in the Login and Signup components. Each component gets its own set of form values and a submit callback specific to its functionality.

### Advantages of Reusing Logic with Custom Hooks:

Reusing logic through custom hooks offers several advantages:

- **Code Organization**: Custom hooks help separate concerns and keep logic centralized, leading to cleaner and more organized codebases.

- **Code Reusability**: By encapsulating reusable logic in custom hooks, we eliminate code duplication and promote code reuse across different components and even projects.

- **Maintenance and Consistency**: Centralizing logic in custom hooks ensures that any updates or improvements made to the hook propagate to all components using it, ensuring consistency and easier maintenance.

### Best Practices for Reusing Logic with Custom Hooks:

To effectively reuse logic with custom hooks, follow these best practices:

- **Documentation**: Provide clear documentation and usage examples for custom hooks to make them more accessible to other developers.

- **Flexibility and Customizability**: Design custom hooks to be flexible and customizable, allowing components to pass in parameters or callbacks to adjust the behavior of the hook.

- **Testing**: Write comprehensive tests for custom hooks to ensure their functionality and prevent regressions when used in different components.

Reusing logic across components is a crucial aspect of building scalable and maintainable React applications. Custom hooks empower developers to extract and reuse common functionality, promoting code reusability, organization, and efficiency. By following best practices and leveraging the flexibility of custom hooks, you can create reusable hooks tailored to specific use cases, ultimately enhancing the development experience and the quality of your React applications.


## Utilizing TypeScript Interfaces with Custom Hooks

When working with React and TypeScript, leveraging TypeScript interfaces with custom hooks brings significant benefits in terms of type safety and code predictability. TypeScript interfaces allow us to define the shape of custom hook parameters and return values, ensuring that our code adheres to specific types and catching type errors during development. In this write-up, we will explore the benefits of using TypeScript interfaces with custom hooks, walk through the process of creating a custom hook that enforces type checks, provide code snippets illustrating their usage, and discuss best practices for defining TypeScript interfaces for custom hooks.

### Benefits of Using TypeScript Interfaces with Custom Hooks

Utilizing TypeScript interfaces with custom hooks offers several advantages:

- **Type Safety**: TypeScript interfaces enable static type checking, catching potential type errors at compile-time and preventing runtime errors.

- **Code Predictability**: By defining the shape of parameters and return values, TypeScript interfaces provide clear expectations for how the custom hook should be used and what it should return.

- **Enhanced Developer Experience**: TypeScript interfaces provide better IDE support, autocompletion, and documentation, improving overall code maintainability and readability.

TypeScript interfaces can be employed to define the shape of the custom hook's parameters and return values. This helps ensure that the data passed into the hook and the expected output adhere to specific types and structures.

### Creating a Custom Hook with TypeScript Interfaces:

Let's walk through an example of creating a custom hook called `useFetchData` that utilizes TypeScript interfaces to enforce type checks:

```tsx
import { useEffect, useState } from 'react';

interface FetchDataOptions {
  url: string;
}

interface FetchDataResult<T> {
  data: T | null;
  loading: boolean;
  error: string | null;
}

export const useFetchData = <T>(options: FetchDataOptions): FetchDataResult<T> => {
  const [data, setData] = useState<T | null>(null);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        const response = await fetch(options.url);
        const jsonData = await response.json() as T;
        setData(jsonData);
      } catch (error) {
        setError(error as string);
      }
      setLoading(false);
    };

    fetchData();
  }, [options.url]);

  return { data, loading, error };
};
```

In this example, we define two TypeScript interfaces: FetchDataOptions to specify the expected options for the API request URL, and FetchDataResult to define the structure of the custom hook's return values. By utilizing these interfaces, we ensure type safety and provide a clear contract for the hook's usage.

### Usage of TypeScript Interfaces with Custom Hooks:

Now, let's see how TypeScript interfaces enhance the usage of the custom hook in TypeScript-enabled components:

```tsx
import React from 'react';
import { useFetchData } from './useFetchData';

interface User {
  id: number;
  name: string;
  email: string;
}

export const UserDetails = () => {
  const { data, loading, error } = useFetchData<User>({
    url: 'https://api.example.com/users/1',
  });

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  return (
    <div>
      <h2>User Details</h2>
      {data && (
        <div>
          <p>ID: {data.id}</p>
          <p>Name: {data.name}</p>
          <p>Email: {data.email}</p>
        </div>
      )}
    </div>
  );
};
```

In this example, we use the `useFetchData` custom hook with the `User` interface, ensuring that the `data` returned from the API call adheres to the expected structure. The usage of TypeScript interfaces provides type safety and allows us to access the properties of the fetched user data without type errors.

### Advantages of Using TypeScript Interfaces with Custom Hooks:

Utilizing TypeScript interfaces with custom hooks brings several advantages:

- **Type Safety**: TypeScript interfaces help catch type errors early during development, reducing the likelihood of runtime errors and enhancing code reliability.

- **Code Maintainability**: With clearly defined interfaces, it becomes easier to understand and maintain code, especially when collaborating with other developers.

- **Documentation and Autocompletion**: TypeScript interfaces improve IDE support, providing autocompletion, type hints, and better documentation, leading to a more efficient and error-free coding experience.


### Best Practices for Defining TypeScript Interfaces for Custom Hooks:

To ensure effective usage of TypeScript interfaces with custom hooks, follow these best practices:

- **Follow Naming Conventions**: Use meaningful names for interfaces that reflect their purpose and the entities they define.

- **Proper Type Annotations**: Provide accurate type annotations for each property within the interface to capture the desired data structure and enforce type checks.
  
- **Keep Interfaces Decoupled**: Define interfaces separately from the custom hooks to promote reusability and decoupling from specific implementations.

Using TypeScript interfaces with custom hooks is a powerful approach to enhance type safety, predictability, and code maintainability in React applications. By leveraging interfaces to define the shape of custom hook parameters and return values, developers can catch type errors early, improve documentation, and provide better IDE support. Following best practices for defining TypeScript interfaces ensures clean and reusable code, leading to more robust and scalable applications.