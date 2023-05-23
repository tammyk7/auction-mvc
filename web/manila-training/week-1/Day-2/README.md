# Advanced Hooks and TypeScript Types

## useMemo and useCallback

The `useMemo` and `useCallback` hooks are similar. The main difference is that `useMemo` returns a memoized value and `useCallback` returns a memoized function.

### What is `useMemo`?

`useMemo` is a React Hook that lets you cache the result of a calculation between re-renders. It accepts 2 arguments - a function `calculations` that calculates a result (usually an expensive calculation), and the `dependencies` array:

```jsx
const memoizedResult = useMemo(calculations, dependencies);
```

During the initial render, `useMemo(calculations, dependencies)` invokes `calculations`, memoizes the result, and returns it to the component.

If the dependencies don't change during the next renderings, then `useMemo()` _doesn't invoke_ `calculations`, but returns the memoized value.

But if the dependencies change during re-rendering, then `useMemo()` invokes `compute`, memoizes the new value, and returns it.

That's the essence of `useMemo()` hook.

If your computation callback uses props or state values, then be sure to indicate these values as dependencies like so:

```jsx
const memoizedResult = useMemo(() => {
  return expensiveFunction(propA, propB);
}, [propA, propB]);
```

Now let's see how it works in an example:

A component `<CalculateFactorial />` calculates the factorial of a number introduced into an input field. Here's a possible implementation of `<CalculateFactorial />` component:

```jsx
import { useState } from 'react';

export const CalculateFactorial = () => {
  const [number, setNumber] = useState(1);
  const [inc, setInc] = useState(0);

  const factorial = factorialOf(number);

  const onChange = (event) => {
    setNumber(Number(event.target.value));
  };

  const onClick = () => setInc(i => i + 1);

  return (
    <div>
      Factorial of
      <input type="number" value={number} onChange={onChange} />
      is {factorial}
      <button onClick={onClick}>Re-render</button>
    </div>
  )
}

function factorialOf(n) {
  console.log('factorialOf(n) called!');
  return n <= 0 ? 1 : n * factorialOf(n - 1);
};
```

Every time you change the input value, the factorial is calculated `factorialOf(n)` and `'factorialOf(n) called!'` is logged to console.

On the other side, each time you click _Re-render_ button, `inc` state value is updated. Updating `inc` state value triggers `<CalculateFactorial />` re-rendering. But, as a secondary effect, during re-rendering the factorial is recalculated again — `'factorialOf(n) called!'` is logged to console.

How can you memoize the factorial calculation when the component re-renders? Welcome `useMemo()` hook!

By using `useMemo(() => factorialOf(number), [number])` instead of simple `factorialOf(number)`, React memoizes the factorial calculation.

Let's improve `<CalculateFactorial />` and memoize the factorial calculation:

```jsx
import { useState, useMemo } from 'react';

export const CalculateFactorial = () => {
  const [number, setNumber] = useState(1);
  const [inc, setInc] = useState(0);

  const factorial = useMemo(() => factorialOf(number), [number]);

  const onChange = event => {
    setNumber(Number(event.target.value));
  };
  const onClick = () => setInc(i => i + 1);
  
  return (
    <div>
      Factorial of 
      <input type="number" value={number} onChange={onChange} />
      is {factorial}
      <button onClick={onClick}>Re-render</button>
    </div>
  );
}

function factorialOf(n) {
  console.log('factorialOf(n) called!');
  return n <= 0 ? 1 : n * factorialOf(n - 1);
}
```

Every time you change the value of the number, `'factorialOf(n) called!'` is logged to console. That's expected.

However, if you click _Re-render_ button, `'factorialOf(n) called!'` isn't logged to console because `useMemo(() => factorialOf(number), [number])` returns the memoized factorial calculation. Great!

**Remember to use memoization with care**

While `useMemo()` can improve the performance of the component, you have to make sure the profile the component with and without the hook. Only after that make the conclusion whether memoization is worth it.

When memoization is used inappropriately, it could harm your application's performance.

Read more on when **NOT** to use `useMemo()` [here](https://blog.logrocket.com/when-not-to-use-usememo-react-hook/#when-not-use-usememo).

### What is `useCallback`?

In React, the `useCallback` hook is used to memoize functions and optimize performance in certain scenarios. It is particularly useful when passing functions as props to child components, as it prevents unnecessary re-rendering of those components.

When a functional component re-renders, any functions defined within it are recreated, even if they have the same content. This can lead to unnecessary re-renders of child components that depend on those functions, as they receive a new reference to the function prop.

Before we dive into `useCallback()` use, let's talk about the problem `useCallback()` solves - functions equality check.

#### Understanding functions equality check

Functions in JavaScript are first-class citizens, meaning that a function is a regular object. The function object can be returned by other functions, be compared, etc.: anything you can do with an object.

Let's write a function `factory()` that returns functions that sum numbers:

```jsx
function factory() {
  return (a, b) => a + b;
}

const sumFunc1 = factory();
const sumFunc2 = factory();

console.log(sumFunc1(1, 2)); // => 3
console.log(sumFunc2(1, 2)); // => 3

console.log(sumFunc1 === sumFunc2); // => false
console.log(sumFunc1 === sumFunc1); // => true
```

`sumFunc1` and `sumFunc2` are function that sum two numbers. They've been created by the `factory()` function.

The functions `sumFunc1` and `sumFunc2` share the same code source, but they are different function objects. Comparing them `sumFunc1 === sumFunc2` evaluates to false. 

This is false because of **referential equality**. We can say two objects are referentially equal when the pointers of the two objects are the same or when the operators are the same object instance. Since `const sumFunc1 = factory()` and `const sumFunc2 = factory()` are different instances of the same function, they are not referentially equal. You can read more on referential equality [here](https://www.geeksforgeeks.org/what-is-object-equality-in-javascript/).

#### Purpose of `useCallback()`

Different function objects sharing the same code are often created inside React components:

```jsx
const MyComponent = () => {
  // handleClick is re-created on each render
  const handleClick = () => {
    console.log('Clicked!');
  };
}
```

`handleClick` is a different function object on every rendering of `MyComponent`.

Because inline functions are cheap, the re-creation of functions on each rendering is not a problem. _A few inline functions per component are acceptable._

But in some cases you need to maintain a single function instance between renderings:

1. A functional component wrapped inside `React.memo()` accepts a function object as prop
2. When the function object is a dependency to other hooks, e.g. `useEffect(..., [callback])`
3. When the function has some internal state, e.g. when the function is debounced or throttled.

That's when `useCallback(callbackFunc, deps)` is helpful: given the same depedency values `deps`, the hook returns the same function instance between renderings (aka memoization):

```jsx
import { useCallback } from 'react';

const MyComponent = () => {
  // handleClick is the same function object
  const handleClick = useCallback(() => {
    console.log('Clicked!');
  }, []);

  // ...
}
```

`handleClick` variable has always the same callback function object between renderings of `MyComponent`.

#### A good use case

Lets take a look at example:

```jsx
import React from 'react';
import useSearch from './fetch-items';

const MyBigList = ({ term, onItemClick }) => {
  const items = useSearch(term);

  const map = item => <div onClick={onItemClick}>{item}</div>;

  return <div>{items.map(map)}</div>;
};

export default React.memo(MyBigList);
```

The list could be big, maybe hundreds or even thousands of items. To prevent useless list re-renderings, you wrap it in a `React.memo()`.

The parent component of `MyBigList` provides a handler function to know when an item is clicked:

```jsx
import { useCallback } from 'react';

export const MyParent = ({ term }) => {
  const onItemClick = useCallback(event => {
    console.log('You clicked ', event.currentTarget);
  }, [term]); 

  return (
    <MyBigList
      term={term}
      onItemClick={onItemClick}
    />
  );
};
```

`onItemClick` callback is memoized by `useCallback()`. As long as `term` is the same, `useCallback()` returns the same function object.

When `MyParent` component re-renders, `onItemClick` function object remains the same and doesn't break the memoization of `MyBigList`.

#### If useCallback memoizes a function why don't we just wrap all functions in a `useCallback()`?

In general, the consensus is that you should only memoize something when it would make a big difference by preventing wasted re-renders. Now sure, in a large codebase, it can be tricky to figure out when it's necessary because the vast amount of moving components.

However, it is important to note that premature optimization has a **cost** and to go with a _use when necessary approach_.

> 💡 What is this cost you ask? Memory. Everything memoized gets stored in memory.

Here's an example of when `useCallback` is unnecessary:

```jsx
import { useCallback } from 'react';

const MyComponent = () => {
  // Contrived use of useCallback()
  const handleClick = useCallback(() => {
    console.log('You clicked');
  }, []);

  return <MyChild onClick={handleClick} />;
}

const MyChild = ({ onClick }) => {
  return <button onClick={onClick}>I am a child</button>;
}
```

This use case exemplifies that _the optimization costs more than not having the optimization_.

[Here's](https://www.joshwcomeau.com/react/usememo-and-usecallback/) an article on `useMemo` and `useCallback` as an additional resource.

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

## Introduction to TypeScript types for React Hooks

<!-- TO ADD -->