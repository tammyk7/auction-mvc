# Advanced Hooks

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

To summarize, these are the benefits of using the hooks `useMemo`:

  1. **Performance optimization**: `useMemo` can be used to cache the expensive calculations or operations, ensuring that they are only recomputed when their dependencies change. This can help improve the performance of your application by avoiding unnecessary computations.

  2. **Memoizing computed values**: If you have a computationally expensive operation that generates a value based on some input, you can use `useMemo` to memoize the result and prevent redundant calculations. This is especially useful when the computation involves complex logic or involves traversing large data structures.

  3. **Avoiding unnecessary renders**: `useMemo` can be used to prevent unnecessary re-rendering of components by memoizing the values passed as props. If a value passed to a component as a prop is derived from a costly computation, you can use `useMemo` to memoize it. This way, the component will only re-render when the memoized value changes.

  4. **Optimizing heavy data transformations**: When dealing with large datasets, `useMemo` can be used to optimize heavy data transformations or filtering operations. By memoizing the transformed data, you can avoid recomputing it on each render, improving the overall performance.

  5. **Memoizing callback functions**: If you have a component that passes a callback function to child components, you can wrap the callback in `useMemo` to memoize it. This ensures that the child components don't re-render unnecessarily if the callback doesn't change.

  6. **Preventing unnecessary API calls**: When fetching data from an API, you can use `useMemo` to memoize the API response, preventing unnecessary API calls when the dependencies haven't changed. This can help reduce network requests and improve the responsiveness of your application.

**Remember to use memoization with care**

While `useMemo()` can improve the performance of the component, you have to make sure the profile the component with and without the hook. Only after that make the conclusion whether memoization is worth it.

When memoization is used inappropriately, it could harm your application's performance.

Read more on when **NOT** to use `useMemo()` [here](https://blog.logrocket.com/when-not-to-use-usememo-react-hook/#when-not-use-usememo).

---

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
