# Advanced Hooks

## useRef and useReducer

### `useRef` hook

The `useRef` hook in React provides a way to create a mutable reference that persists across renders of a functional component. Unlike state variables managed by `useState`, a `ref` value managed by `useRef` does not trigger re-renders when its value changes. Instead, it allows you to store and access mutable values that persist throughout the component's lifecycle.

The `useRef` hook can be used for various purposes, including:

1. **Referencing DOM elements:** One of the most common use cases of `useRef` is to reference DOM elements directly. You can use the `ref` attribute of a JSX element to assign a `ref` object created using `useRef`. This allows you to access and manipulate the DOM element imperatively.

    ```jsx
    import React, { useRef } from 'react';

    const ExampleComponent = () => {
      const inputRef = useRef(null);

      const focusInput = () => {
        inputRef.current.focus();
      };

      return (
        <div>
          <input ref={inputRef} type="text" />
          <button onClick={focusInput}>Focus Input</button>
        </div>
      );
    };
    ```

    In this example, the `inputRef` is created using `useRef`. The `ref` attribute of the `<input>` element is set to `inputRef`, allowing you to access the input element using `inputRef.current`. Clicking the "Focus Input" button will trigger the `focusInput` function, which uses `inputRef.current` to focus the input element.

<br />

2. **Preserving values between renders:** Another use case for `useRef` is to preserve a value between renders without triggering re-renders. This can be useful for storing and accessing values that don't affect the component's UI but need to persist across renders.

    ```jsx
    import React, { useState, useEffect, useRef } from 'react';

    const ExampleComponent = () => {
      const [count, setCount] = useState(0);
      const prevCountRef = useRef(null);

      useEffect(() => {
        prevCountRef.current = count;
      }, [count]);

      return (
        <div>
          <p>Current Count: {count}</p>
          <p>Previous Count: {prevCountRef.current}</p>
          <button onClick={() => setCount(count + 1)}>Increment</button>
        </div>
      );
    };
    ```

    In this example, `prevCountRef` is created using `useRef`. Inside the `useEffect` hook, whenever the `count` state changes, the `prevCountRef.current` is updated to store the previous count value. This allows you to display the current count and the previous count in the component's UI without triggering re-renders when the previous count changes.

<br />

3. **Preserving mutable values across function calls:** `useRef` can also be used to preserve mutable values across multiple calls to a function within a component. Since the ref object persists across renders, it allows you to maintain a value that remains the same throughout the component's lifecycle.

    ```jsx
    import React, { useRef } from 'react';

    const ExampleComponent = () => {
      const refCount = useRef(0);

      const incrementAndLog = () => {
        refCount.current += 1;
        console.log('Current count:', refCount.current);
      };

      return (
        <div>
          <button onClick={incrementAndLog}>Increment and Log</button>
        </div>
      );
    };
    ```

    In this example, the `refCount` is created using `useRef` and initialized with an initial value of 0. The incrementAndLog function increments the `refCount.current` value and logs it to the console. The `refCount.current` value is preserved across multiple function calls, allowing you to maintain a count that persists across renders without triggering re-renders.

These are just a few examples of how `useRef` can be used in React. It provides a way to store mutable values, reference DOM elements, and preserve values between renders or function calls. Remember that changes to `ref.current` do not trigger re-renders, so it should be used for values that don't affect the component's UI.

---

### `useReducer` hook

The `useReducer` hook in React provides a way to manage complex state and state transitions within a functional component. It is an alternative to using the useState hook when you have state logic that involves multiple actions and complex state updates.

The `useReducer` hook takes in a reducer function and an initial state, and returns an array with two elements: the current state and a dispatch function. The reducer function accepts the current state and an action as arguments, and returns the new state based on the action.

The `useReducer` hook is typically used for managing state that follows a pattern of actions and transitions. It helps in keeping the state management logic centralized and makes it easier to reason about state changes.

Here are a few example use cases of `useReducer` with code snippets:

1. **Managing a counter with increment and decrement actions:**

    ```jsx
    import React, { useReducer } from 'react';

    const initialState = 0;

    const reducer = (state, action) => {
      switch (action.type) {
        case 'INCREMENT':
          return state + 1;
        case 'DECREMENT':
          return state - 1;
        default:
          return state;
      }
    };

    const Counter = () => {
      const [count, dispatch] = useReducer(reducer, initialState);

      return (
        <div>
          <p>Count: {count}</p>
          <button onClick={() => dispatch({ type: 'INCREMENT' })}>Increment</button>
          <button onClick={() => dispatch({ type: 'DECREMENT' })}>Decrement</button>
        </div>
      );
    };
    ```

    In this example, the `useReducer` hook is used to manage the counter state. The reducer function takes in the current state and an action object with a `type` property. Depending on the action type, the reducer updates the state accordingly. The `dispatch` function is used to trigger the state updates by passing the corresponding action to it.

    <br />

2. **Managing a form state with multiple input fields:**

    ```jsx
    import React, { useReducer } from 'react';

    const initialState = {
      name: '',
      email: '',
      password: '',
    };

    const reducer = (state, action) => {
      return {
        ...state,
        [action.field]: action.value,
      };
    };

    const Form = () => {
      const [formData, dispatch] = useReducer(reducer, initialState);

      const handleChange = (event) => {
        const { name, value } = event.target;
        dispatch({ field: name, value });
      };

      return (
        <form>
          <input type="text" name="name" value={formData.name} onChange={handleChange} />
          <input type="email" name="email" value={formData.email} onChange={handleChange} />
          <input type="password" name="password" value={formData.password} onChange={handleChange} />
        </form>
      );
    };
    ```

    In this example, useReducer is used to manage the form state with multiple input fields. The reducer function updates the corresponding field in the state based on the action dispatched with the field name and value. The handleChange function captures the input changes and dispatches the corresponding action to update the form state.
    
    <br />
    
3. **Managing a todo list with add, delete, and toggle actions:**

    ```jsx
    import React, { useReducer } from 'react';

    const initialState = [];

    const reducer = (state, action) => {
      switch (action.type) {
        case 'ADD_TODO':
          return [...state, { id: Date.now(), text: action.text, completed: false }];
        case 'DELETE_TODO':
          return state.filter((todo) => todo.id !== action.id);
        case 'TOGGLE_TODO':
          return state.map((todo) => (todo.id === action.id ? { ...todo, completed: !todo.completed } : todo));
        default:
          return state;
      }
    };

    const TodoList = () => {
      const [todos, dispatch] = useReducer(reducer, initialState);

      const addTodo = (text) => {
        dispatch({ type: 'ADD_TODO', text });
      };

      const deleteTodo = (id) => {
        dispatch({ type: 'DELETE_TODO', id });
      };

      const toggleTodo = (id) => {
        dispatch({ type: 'TOGGLE_TODO', id });
      };

      return (
        <div>
          <ul>
            {todos.map((todo) => (
              <li key={todo.id}>
                <span style={{ textDecoration: todo.completed ? 'line-through' : 'none' }}>{todo.text}</span>
                <button onClick={() => toggleTodo(todo.id)}>Toggle</button>
                <button onClick={() => deleteTodo(todo.id)}>Delete</button>
              </li>
            ))}
          </ul>
          <input type="text" />
          <button onClick={addTodo}>Add Todo</button>
        </div>
      );
    };
    ```

    In this example, `useReducer` is used to manage a todo list. The reducer function handles three types of actions: adding a new todo, deleting a todo by ID, and toggling the completed state of a todo. The corresponding actions are dispatched using the `dispatch` function. The component renders the list of todos and provides input and buttons for adding, deleting, and toggling todos.

These examples demonstrate how `useReducer` can be used to manage complex state and state transitions in React. It helps in keeping the state logic organized, centralizing the state updates, and improving code maintainability.
