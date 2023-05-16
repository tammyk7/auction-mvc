# Day 1: React Functional Components & Hooks:

## Quick review of React functional components

React functional components are a modern and widely used approach to building user interfaces in React. They provide a more concise and readable way to create components compared to traditional class components. With functional components, you can define reusable UI elements using regular JavaScript functions.

React functional components have become the preferred way to build UI components in React. They offer a more concise and readable syntax compared to traditional class components. Let's dive into some key aspects of functional components:

1. **Simplicity and Readability:**

    Functional components are regular JavaScript functions that return JSX (JavaScript XML) to describe the component's UI. Here's an example of a simple functional component:

    ```jsx
    import React from 'react';

    const Greeting = () => {
      return <h1>Hello, world!</h1>;
    };
    ```

    As you can see, the Greeting component is defined as a function that returns JSX, representing the desired UI. This concise syntax makes functional components easy to read and understand.

<br />

2. **State and Lifecycle with Hooks:**

    React introduced hooks as a way to manage state and other React features in functional components. Hooks allow you to write complex component logic without using class components. Here's an example of a functional component using the `useState` hook:

    ```jsx
    import React, { useState } from 'react';

    const Counter = () => {
      const [count, setCount] = useState(0);

      const increment = () => {
        setCount(count + 1);
      };

      return (
        <div>
          <p>Count: {count}</p>
          <button onClick={increment}>Increment</button>
        </div>
      );
    };
    ```

    In this example, the `useState` hook is used to add state to the `Counter` component. It returns a state variable (`count`) and a function (`setCount`) to update the state. Clicking the "Increment" button updates the count and triggers a re-render.

    We'll expore more on React hooks later on.

<br />

3. **Compatibility and Ecosystem:**

    Functional components seamlessly integrate with the wider React ecosystem, including popular libraries and tools. They work well with React Router, Redux, and other React-based libraries. Here's an example using React Router with a functional component:

    ```jsx
    import React from 'react';
    import { BrowserRouter, Route, Link } from 'react-router-dom';

    const Home = () => <h1>Welcome to the Home page!</h1>;
    const About = () => <h1>Learn more about us!</h1>;

    const App = () => {
      return (
        <BrowserRouter>
          <nav>
            <ul>
              <li>
                <Link to="/">Home</Link>
              </li>
              <li>
                <Link to="/about">About</Link>
              </li>
            </ul>
          </nav>
          <Route exact path="/" component={Home} />
          <Route path="/about" component={About} />
        </BrowserRouter>
      );
    };
    ```

    Here, functional components (`Home` and `About`) are used as route components within a React Router setup. This showcases the compatibility of functional components with other React libraries.

    Functional components, combined with hooks, simplify component development, enhance performance, and integrate seamlessly with the React ecosystem. If you're already familiar with React, transitioning to functional components is straightforward and allows you to leverage your existing knowledge while embracing the benefits they offer.

<hr>

## Introduction to React hooks (`useState`, `useEffect`, `useContext`)

Hooks were first introduced in React 16.8. They're great because they let you use more of React's features – like managing your component's state, or performing an after effect when certain changes occur in state(s) without writing a class.

### What is the `useState` hook?

The state of your application is bound to change at some point. This could be the value of a variable, an object, or whatever type of data exists in your component.

To make it possible to have the changes reflected in the DOM, we have to use a React hook called `useState`. It looks like this:

```jsx
import React, { useState } from 'react';

const Counter = () => {
  const [count, setCount] = useState(0);

  const increment = () => {
    setCount(count + 1);
  };

  return (
    <div>
      <p>Count: {count}</p>
      <button onClick={increment}>Increment</button>
    </div>
  );
};
```

To be able to use this hook, you have to import the `useState` hook from React. We are using a functional component called `Counter`.

```jsx
import { useState } from "react";

const [count, setCount] = useState(0);
```

After that, you have to create your state and give it an initial value (or initial state) which is `0`. The state variable is called `count`, and `setCount` is the function for updating its value.

Having a good understanding of some of the ES6 features will help you grasp the basic functionalities of React. Above, we used the destructuring assignment to assign an initial count value to the state in `useState(0)`.

> 💡 The `[` and `]` syntax here is called [array destructuring](https://javascript.info/destructuring-assignment) and it lets you read values from an array. The array returned by `useState` always has exactly two items.

```jsx
const increment = () => {
  setCount(count + 1);
};

return (
  <div>
    <p>Count: {count}</p>
    <button onClick={increment}>Increment</button>
  </div>
);
```

Next, the DOM has a paragraph containing the name variable and a button which fires a function when clicked. The `increment()` function calls the `setCount()` function which then changes the values of the name variable to the value passed in to the `setCount()` function.

**The values of your state must not be hard coded.**

Read more about `useState` and how a component's memory work [here](https://react.dev/learn/state-a-components-memory).

### What is the `useEffect` hook?

The Effect Hook, just like the name implies, carries out an effect each time there is a state change. By default, it runs after the first render and every time the state is updated.

Looking at our example code, we create a state variable `count` with an initial value of zero. A button in the DOM will increase the value of this variable by one every time it is clicked. The `useEffect` hook will run every time the `count` variable changes and then log out some information to the console.

```jsx
import { useState, useEffect } from "react";

const Counter = () => {
  const [count, setCount] = useState(0);

  const increment = () => setCount(count + 1);

  useEffect(() => {
    console.log(`You have clicked the button ${count} times`);
  })

  return (
    <div>
      <p>Count: {count}</p>
      <button onClick={increment}>Increment</button>
    </div>
  );
}
```

The first line of code where you import the required hook(s) is always important if you are going to "hook" into this React feature. We imported the two hooks we used above:

```jsx
import React, { useState, useEffect } from "react";
```

Note that you can use the useEffect hook to achieve various effects like fetching data from an external API (which you will see in another section of this article), changing the DOM in your component, and so on.

#### `useEffect` Dependencies

But what happens if you want your effect to run only after the first render, or if you have multiple states and only want an after effect attached to one of the states?

We can do this by using a dependency array which is passed in as a second argument in the `useEffect` hook.

#### How to run an "effect" once

```jsx
import { useState, useEffect } from "react";

const Counter = () => {
  const [count, setCount] = useState(0);

  const increment = () => setCount(count + 1);

  useEffect(() => {
    console.log(`You have clicked the button ${count} times`);
  }, [])

  return (
    <div>
      <p>Count: {count}</p>
      <button onClick={increment}>Increment</button>
    </div>
  );
}
```

The code above is the same as in the previous section, except that the useEffect hook accepts an empty array `[]` as a second argument. When we leave the array empty, the effect will only run once irrespective of the changes to the state it is attached to.

#### How to attach an effect to a particular state

```jsx
import { useState, useEffect } from "react";

const Counter = () => {
  const [count, setCount] = useState(0);
  const [count2, setCount2] = useState(0);

  const increment = () => setCount(count + 1);
  const increment2 = () => setCount2(count2 + 1);

  useEffect(() => {
    console.log(`You have clicked the first button ${count} times`);
  }, [count])

  useEffect(() => {
    console.log(`You have clicked the second button ${count2} times`);
  }, [count2])

  return (
    <div>
      <p>Count: {count}</p>
      <button onClick={increment}>Increment</button>
      <br />
      <p>Count2: {count2}</p>
      <button onClick={increment2}>Increment</button>
    </div>
  );
}
```

In the code above, we created two states and two useEffect hooks. Each state has an after effect attached to it by passing the name of the state `[count]` and `[count2]` to the corresponding useEffect array dependency.

So when the state of `count` changes, the useEffect hook responsible for watching these changes will carry out any after effect assigned to it. Same applies to `count2`.

### What is the `useContext` hook?

The `useContext` hook is a powerful feature in React that allows you to consume values from a Context provider in a more convenient way. It eliminates the need for prop drilling, making it easier to access shared data or functionality throughout your component tree.

1. **Creating a Context:**

    To use `useContext`, you first need to create a Context. Here's an example of creating a simple `ThemeContext`:

    ```jsx
    import React from 'react';

    const ThemeContext = React.createContext();

    export default ThemeContext;
    ```

<br />

2. **Providing a Context Value:**

    You can provide a value to the context using a `Provider` component. Here's an example where we provide a theme value to the `ThemeContext`:

    ```jsx
    import React from 'react';
    import ThemeContext from './ThemeContext';

    const App = () => {
      const theme = 'light';

      return (
        <ThemeContext.Provider value={theme}>
          {/* Your component tree */}
        </ThemeContext.Provider>
      );
    };

    export default App;
    ```

<br />

3. **Consuming the Context Value:**
    Now, you can consume the context value using the `useContext` hook within any child component of the `Provider`. Here's an example of a `Header` component consuming the `theme` value from the `ThemeContext`:

    ```jsx
    import React, { useContext } from 'react';
    import ThemeContext from './ThemeContext';

    const Header = () => {
      const theme = useContext(ThemeContext);

      return <header style={{ backgroundColor: theme }}>My App Header</header>;
    };

    export default Header;
    ```
    In this example, the `useContext` hook is used to access the `theme` value from the `ThemeContext` provider. This simplifies accessing the context value within the component without explicitly passing it down as props.

<br />

4. **Multiple Contexts:**
    You can also use multiple contexts within your component tree. Each context can have its own provider and be consumed by the relevant components. Here's an example of using multiple contexts:

    ```jsx
    import React from 'react';
    import ThemeContext from './ThemeContext';
    import UserContext from './UserContext';

    const App = () => {
      const theme = 'light';
      const user = { name: 'John Doe' };

      return (
        <ThemeContext.Provider value={theme}>
          <UserContext.Provider value={user}>
            {/* Your component tree */}
          </UserContext.Provider>
        </ThemeContext.Provider>
      );
    };

    export default App;
    ```

    To consume multiple contexts, you can use multiple `useContext` hooks within your components, each corresponding to a different context.

    The `useContext` hook simplifies the consumption of values from a context, reducing the need for prop drilling and making your code more readable and maintainable. It's a powerful tool to create reusable and decoupled components within your React applications.