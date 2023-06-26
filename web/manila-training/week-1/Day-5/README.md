### Day 5: React Context & Performance Optimization with TypeScript

React Context is a feature in React that allows you to share data between components without explicitly passing it through props. It provides a way to create global or local "contexts" that can hold state and pass it down the component tree, making it accessible to any component that needs it.

Context is especially useful when you have data that needs to be accessed by multiple components at different levels of the component hierarchy. Instead of passing props through each intermediate component, you can use Context to provide the data at a higher level and consume it at a lower level without prop drilling.

The key components of React Context are:

Provider: The Provider component is responsible for providing the context value to its descendant components. It wraps around the component hierarchy and accepts a value prop, which represents the data or state to be shared.

Consumer: The Consumer component is used to access the context value within a component. It uses a render prop or a function as its child to access and use the context value.

useContext Hook: Introduced in React 16.8, the useContext hook is a simpler and more concise way to consume context values within a functional component. It allows you to access the current context value directly without the need for a Consumer component.

Here's an example to illustrate the usage of React Context:

```jsx
import React, { createContext, useContext } from 'react';

// Create a new context
const MyContext = createContext();

// Define a provider component
const MyProvider = ({ children }) => {
  const sharedData = 'This is the shared data';

  return (
    <MyContext.Provider value={sharedData}>
      {children}
    </MyContext.Provider>
  );
};

// Consume the context value in a child component
const MyChildComponent = () => {
  const sharedData = useContext(MyContext);

  return <p>{sharedData}</p>;
};

// Usage in the app
const App = () => {
  return (
    <MyProvider>
      <MyChildComponent />
    </MyProvider>
  );
};
```

In this example, we create a new context using createContext(). Then, we wrap the component tree inside the MyProvider component, which provides the context value to its descendants using the value prop. The MyChildComponent consumes the context value using the useContext hook, which allows direct access to the shared data.

React Context provides a convenient way to manage shared state or data across components, reducing the complexity of prop drilling and making your code more concise and maintainable.

React Context is a powerful tool for sharing state across components, but let's consider performance optimization when using it in conjunction with TypeScript.

1. **Context Value Memoization:**
    When using React Context, it's crucial to memoize the context value to prevent unnecessary re-renders of consuming components. Memoization ensures that the value remains the same if its dependencies haven't changed. You can leverage libraries like memoize-one or utilize React's built-in useMemo hook to memoize the context value.

    ```tsx
    import React, { createContext, useMemo } from 'react';

    interface MyContextValue {
      // Context value properties
    }

    const MyContext = createContext<MyContextValue | undefined>(undefined);

    // Memoize the context value
    const memoizedValue = useMemo(() => {
      // Create and return the context value object
    }, [dependencies]);

    const MyComponent: React.FC = () => {
      return (
        <MyContext.Provider value={memoizedValue}>
          {/* Render your components */}
        </MyContext.Provider>
      );
    };
    ```

    By memoizing the context value, you can ensure that the value doesn't change unless its dependencies change, resulting in improved performance.

2. **Avoiding Unnecessary Context Updates:**

    React Context triggers updates to consuming components whenever the context value changes. To optimize performance, make sure to only update the context value when necessary. You can do this by using the appropriate dependencies in the dependency arrays in your memoized value or using memoized selectors to derive context values.

    For example, consider a scenario where a context value depends on an expensive calculation or an API call. You can use useMemo or memoization techniques to ensure that the calculation or API call is only performed when the dependencies change.

    ```tsx
    // Memoized context value
    const myExpensiveCalculationOrAPICall = useMemo(() => {
      // Create and return the context value object
    }, [dependencies]);
    ```

3. **Using Selective Context Consumers:**
    In some cases, not all components consuming a context need to re-render when the context value changes. By using selective context consumers, you can optimize performance by restricting updates to only the necessary components.

    One approach is to create multiple context providers, each responsible for a specific subset of the context value. This allows components to subscribe to only the relevant context provider and avoid unnecessary re-renders caused by changes in unrelated context values.

4. **TypeScript Type Inference:**
    TypeScript offers static type checking, which can help identify potential performance issues and type-related bugs during development. By leveraging TypeScript's type inference capabilities, you can catch errors early and optimize performance at compile time.

    Ensure that you define and utilize appropriate TypeScript types for your context values, props, and state. This helps prevent type-related runtime errors and improves performance by reducing type-related checks and conversions.

5. **Performance Profiling and Optimization:**

    To ensure optimal performance, it's important to profile and measure the performance of your application. React provides tools like React DevTools and Performance Profiler to analyze component rendering, identify slow components, and optimize performance bottlenecks.

By using these profiling tools, you can identify performance issues, optimize expensive operations, and ensure your React Context implementation performs efficiently.

## Use `useContext` and `useReducer` for global state management

React Context, along with the `useContext` and `useReducer` hooks, can be a powerful combination for global state management in React applications. This approach allows you to share state and dispatch functions across different components without having to pass props through multiple levels of nesting. Let's dive right into an example:

1. Set up Context:

    To begin, let's create a new file called `AppContext.tsx`. This file will define our context and provide the initial state and dispatch function.

    ```tsx
    import React, { createContext, useReducer } from 'react';

    // Define the initial state
    interface AppState {
      counter: number;
    }

    const initialState: AppState = {
      counter: 0,
    };

    // Define the actions for state updates
    type Action = { type: 'INCREMENT' } | { type: 'DECREMENT' };

    // Create the context
    export const AppContext = createContext<{
      state: AppState;
      dispatch: React.Dispatch<Action>;
    }>({
      state: initialState,
      dispatch: () => {},
    });

    // Define the reducer function
    const reducer = (state: AppState, action: Action): AppState => {
      switch (action.type) {
        case 'INCREMENT':
          return { ...state, counter: state.counter + 1 };
        case 'DECREMENT':
          return { ...state, counter: state.counter - 1 };
        default:
          return state;
      }
    };

    // Create the context provider component
    export const AppProvider: React.FC = ({ children }) => {
      const [state, dispatch] = useReducer(reducer, initialState);

      return (
        <AppContext.Provider value={{ state, dispatch }}>
          {children}
        </AppContext.Provider>
      );
    };
    ```

    In this code snippet, we define the `AppState` interface, which represents the shape of our application state. We also define the initial state and the possible actions that can be dispatched to update the state. The `AppContext` is created using `createContext`, and we provide an initial context value with the `initialState` and an empty dispatch function.

    Next, we create the `reducer` function, which takes the current state and an action and returns the new state based on the action type. The `useReducer` hook is then used to create the state and dispatch function, with the reducer function and initialState as arguments.

    Finally, we create the AppProvider component, which wraps the AppContext.Provider component around the children prop. This component provides the state and dispatch function to its children using the value prop.

2. **Consuming the Context**

    Now that we have set up the context, let's see how we can consume it in our components using the `useContext` hook.

    ```tsx
    import React, { useContext } from 'react';
    import { AppContext } from './AppContext';

    const Counter: React.FC = () => {
      const { state, dispatch } = useContext(AppContext);

      const handleIncrement = () => {
        dispatch({ type: 'INCREMENT' });
      };

      const handleDecrement = () => {
        dispatch({ type: 'DECREMENT' });
      };

      return (
        <div>
          <p>Counter: {state.counter}</p>
          <button onClick={handleIncrement}>Increment</button>
          <button onClick={handleDecrement}>Decrement</button>
        </div>
      );
    };

    const App: React.FC = () => {
      return (
        <AppProvider>
          <Counter />
        </AppProvider>
      );
    };

    export default App;
    ```

    In this code snippet, we import the `AppContext` from the `AppContext.tsx` file. Then, using the `useContext` hook, we access the `state` and `dispatch` function from the context.

    Inside the `Counter` component, we define two event handlers: `handleIncrement` and `handleDecrement`. These event handlers dispatch the corresponding actions to update the state. The current counter value is displayed from the `state` object obtained from the context.

    Finally, in the `App` component, we wrap the `Counter` component with the `AppProvider` component to provide the global state and dispatch function to the `Counter` component and its descendants.

    That's it! You now have a basic setup for global state management using React Context, `useContext`, and `useReducer` with TypeScript. Components can consume the global state and dispatch actions to update it using the provided context. Feel free to extend this setup to include more state properties and actions as per your application's requirements.