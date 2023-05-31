# Using RxJS observables in useState and useEffect hooks

Sure, let's start with using RxJS observables in `useState` and `useEffect` hooks without `react-rxjs`.

### Without react-rxjs

You can use RxJS observables with React's `useState` and `useEffect` hooks to manage state and side effects in your components. Here's an example of a component that subscribes to an RxJS observable and updates its state whenever the observable emits a new value:

```jsx
import React, { useState, useEffect } from 'react';
import { interval } from 'rxjs';

function Timer() {
  const [seconds, setSeconds] = useState(0);

  useEffect(() => {
    const subscription = interval(1000).subscribe(setSeconds);
    return () => subscription.unsubscribe();
  }, []);

  return <div>Seconds: {seconds}</div>;
}
```

In this example, `interval(1000)` returns an observable that emits a new value every second. The `useEffect` hook is used to subscribe to this observable when the component mounts, and the `setSeconds` function (returned by `useState`) is used to update the component's state whenever the observable emits a new value. The subscription is cleaned up when the component unmounts.

### With react-rxjs

With `react-rxjs`, you can use the `bind` function to create a hook that subscribes to an RxJS observable and returns its latest value. This simplifies the code and eliminates the need to manually manage the subscription. Here's how you could rewrite the above example using `react-rxjs`:

```jsx
import React from 'react';
import { interval } from 'rxjs';
import { bind } from '@react-rxjs/core';

const [useSeconds] = bind(interval(1000), 0);

function Timer() {
  const seconds = useSeconds();

  return <div>Seconds: {seconds}</div>;
}
```

In this example, `bind(interval(1000), 0)` returns a hook (`useSeconds`) that subscribes to the `interval(1000)` observable and returns its latest value. The hook automatically manages the subscription, so you don't need to use `useEffect` to subscribe and unsubscribe.

Remember, when using `react-rxjs`, you need to ensure that a subscription to the underlying observable is present before the hook is executed. This can be done by wrapping the component that uses the hook in a `Subscribe` component:

```jsx
import { Subscribe } from '@react-rxjs/core';

function App() {
  return (
    <Subscribe>
      <Timer />
    </Subscribe>
  );
}
```

This ensures that the `useSeconds` hook has a subscription to the `interval(1000)` observable when it's executed.

<hr>

### Using RxJS with useEffect for Side Effects

You can also use RxJS observables in `useEffect` to handle side effects in your components. Here's an example of a component that makes an HTTP request using the `ajax` function from `rxjs/ajax` whenever a prop changes:

```jsx
import React, { useState, useEffect } from 'react';
import { ajax } from 'rxjs/ajax';

function User({ id }) {
  const [user, setUser] = useState(null);

  useEffect(() => {
    const subscription = ajax.getJSON(`https://api.github.com/users/${id}`)
      .subscribe(setUser);

    return () => subscription.unsubscribe();
  }, [id]);

  if (!user) {
    return <div>Loading...</div>;
  }

  return <div>{user.name}</div>;
}
```

In this example, `ajax.getJSON(`https://api.github.com/users/${id}`)` returns an observable that emits the response of the HTTP request. The `useEffect` hook is used to subscribe to this observable whenever the `id` prop changes, and the `setUser` function (returned by `useState`) is used to update the component's state whenever the observable emits a new value. The subscription is cleaned up when the component unmounts or when the `id` prop changes.

### With react-rxjs

With `react-rxjs`, you can use the `bind` function to create a hook that subscribes to an RxJS observable and returns its latest value. This simplifies the code and eliminates the need to manually manage the subscription. Here's how you could rewrite the above example using `react-rxjs`:

```jsx
import React from 'react';
import { ajax } from 'rxjs/ajax';
import { bind } from '@react-rxjs/core';

const [useUser] = bind((id) => ajax.getJSON(`https://api.github.com/users/${id}`), null);

function User({ id }) {
  const user = useUser(id);

  if (!user) {
    return <div>Loading...</div>;
  }

  return <div>{user.name}</div>;
}
```

In this example, ``bind((id) => ajax.getJSON(`https://api.github.com/users/${id}`), null)`` returns a hook (`useUser`) that takes an `id` as an argument and subscribes to the ``ajax.getJSON(`https://api.github.com/users/${id}`)`` observable, returning its latest value. The hook automatically manages the subscription, so you don't need to use `useEffect` to subscribe and unsubscribe.

*** Remember, when using `react-rxjs`, you need to ensure that a subscription to the underlying observable is present before the hook is executed. This can be done by wrapping the component that uses the hook in a `Subscribe` component:

```jsx
import { Subscribe } from '@react-rxjs/core';

function App() {
  return (
    <Subscribe>
      <User id={1} />
    </Subscribe>
  );
}
```

This ensures that the `useUser` hook has a subscription to the ``ajax.getJSON(`https://api.github.com/users/${id}`)`` observable when it's executed.

<hr>

## Connecting Observables to Components

In RxJS, an observable is a representation of a stream of data that can be observed by multiple subscribers. In React, we can use observables to manage and update the state of our components. Let's start by creating an observable that emits a random number every second. We'll then connect this observable to our React component.

```jsx
import React, { useEffect, useState } from "react";
import { interval } from "rxjs";

const RandomNumber = () => {
  const [number, setNumber] = useState(null);

  useEffect(() => {
    const subscription = interval(1000).subscribe((val) => {
      setNumber(Math.random());
    });
    return () => subscription.unsubscribe();
  }, []);

  return <div>{number}</div>;
};

export default RandomNumber;
```

In the code above, we import the `interval` operator from RxJS and use it to create an observable that emits a value every second. We then subscribe to this observable and update the state of our component with the emitted value. Finally, we return the updated state as a UI element.

We can now use the `RandomNumber` component in our app:

```javascript
import React from "react";
import RandomNumber from "./RandomNumber";

const App = () => {
  return (
    <div>
      <RandomNumber />
    </div>
  );
};

export default App;
```

## Streaming State with RxJS

In the previous example, we used an observable to update the state of a single component. However, in larger applications, we may need to share state between multiple components. One way to achieve this is by using observables to stream state across our application.

Let's create a simple example where we have a `Counter` component that increments a value on every click. We'll then use an observable to stream this value to another component called `Display`.

```javascript
import React, { useState } from "react";
import { BehaviorSubject } from "rxjs";

const counter$ = new BehaviorSubject(0);

const Counter = () => {
  const [value, setValue] = useState(0);

  const handleClick = () => {
    const newValue = value + 1;
    setValue(newValue);
    counter$.next(newValue);
  };

  return <button onClick={handleClick}>Click me</button>;
};

export default Counter;
```

In the code above, we create a new `BehaviorSubject` observable called `counter$` with an initial value of 0. We also create a `Counter` component that updates the state of its own value and emits the new value to the `counter$` observable.

Next, let's create a new component called `Display` that subscribes to the `counter$` observable and displays the current value:

```jsx
import React, { useEffect, useState } from "react";
import { filter } from "rxjs/operators";
import { counter$ } from "./Counter";

const Display = () => {
  const [value, setValue] = useState(0);

  useEffect(() => {
    const subscription = counter$
      .pipe(filter((val) => val % 2 === 0))
      .subscribe((val) => {
        setValue(val);
      });
    return () => subscription.unsubscribe();
  }, []);

  return <div>{value}</div>;
};

export default Display;
```

In the code above, we import the `filter` operator from RxJS and use it to filter out odd values from the `counter$` observable. We then subscribe to this observable and update the state of our component with the filtered value. Finally, we return the updated state as a UI element.

We can now use the `Counter` and `Display` components in our app:

```javascript
import React from "react";
import Counter from "./Counter";
import Display from "./Display";

const App = () => {
  return (
    <div>
      <Counter />
      <Display />
    </div>
  );
};

export default App;
```

## Sharing State with Subjects

In the previous example, we used a `BehaviorSubject` to stream state across our application. However, there are other types of subjects that we can use depending on our use case.

Here's a quick overview of the different types of subjects:

- `Subject`: A basic subject that can emit values to multiple subscribers. However, it doesn't have an initial value and only emits values that are emitted after subscription.
- `BehaviorSubject`: A subject that has an initial value and emits the last emitted value to new subscribers.
- `ReplaySubject`: A subject that emits multiple values to new subscribers. The number of values emitted depends on the `bufferSize` argument passed to its constructor.
- `AsyncSubject`: A subject that emits the last value emitted before completion to new subscribers.

Let's modify our previous example to use a `Subject` instead of a `BehaviorSubject`:

```jsx
import React, { useState } from "react";
import { Subject } from "rxjs";

const counter$ = new Subject();

const Counter = () => {
  const [value, setValue] = useState(0);

  const handleClick = () => {
    const newValue = value + 1;
    setValue(newValue);
    counter$.next(newValue);
  };

  return <button onClick={handleClick}>Click me</button>;
};

export default Counter;
```

In the code above, we use a `Subject` instead of a `BehaviorSubject`. This means that the `counter$` observable doesn't have an initial value and only emits values that are emitted after subscription.

We can now use the `Counter` and `Display` components in our app:

```jsx
import React from "react";
import Counter from "./Counter";
import Display from "./Display";

const App = () => {
  return (
    <div>
      <Counter />
      <Display />
    </div>
  );
};

export default App;
```

## Using RxJS with useContext
The `useContext` hook allows us to consume values from a context in functional components. We can use RxJS to create a context that emits new values every time it changes.

Let's create a simple theme component that uses RxJS and `useContext` to change the theme based on the current time.

```jsx
import { useContext } from 'react';
import { BehaviorSubject, createContext } from 'rxjs';

const theme$ = new BehaviorSubject('light');
const ThemeContext = createContext();

function ThemeProvider({ children }) {
  const [theme, setTheme] = useState('light');

  theme$.subscribe((value) => {
    setTheme(value);
  });

  return (
    <ThemeContext.Provider value={theme$}>
      {children}
    </ThemeContext.Provider>
  );
}

function Theme() {
  const theme$ = useContext(ThemeContext);

  useEffect(() => {
    const timeSubscription = interval(1000).subscribe(() => {
      const hours = new Date().getHours();
      if (hours >= 6 && hours < 18) {
        theme$.next('light');
      } else {
        theme$.next('dark');
      }
    });

    return () => {
      timeSubscription.unsubscribe();
    };
  }, []);

  return <div>Current theme: {theme$.value}</div>;
}
```

In the above code, we import the `useContext` hook from React and the `BehaviorSubject` class and `createContext` function from RxJS. We createContext function from RxJS. We create a new context using `createContext` function. We `useContext` the context created by `createContext()`. We subscribe to the `theme$` observable and initialize a new observable using new `BehaviorSubject('light')`.

We define a `ThemeProvider` component that subscribes to the observable and updates the theme state using setTheme. We pass the observable to the `ThemeContext.Provider` component as a value.

We define a Theme component that consumes the theme observable using `useContext`. We create an observable using `interval(1000)` that emits a value every second. We subscribe to this observable and update the theme observable based on the current time.

Finally, we return a `div` element that displays the current theme value.




## Using RxJS with Custom Hooks
We can also create custom hooks using RxJS to encapsulate complex logic and reuse it across our application. Here's an example of a custom hook that uses an observable to fetch data from an API:

```jsx
import { useState, useEffect } from "react";
import { ajax } from "rxjs/ajax";
import { map } from "rxjs/operators";

const useApi = (url) => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const subscription = ajax.getJSON(url).pipe(map((response) => response.data)).subscribe((data) => {
      setData(data);
      setLoading(false);
    });
    return () => subscription.unsubscribe();
  }, [url]);

  return [data, loading];
};

export default useApi;
```

In the code above, we use the `useEffect` hook to subscribe to an observable that fetches data from an API and updates the state of our component with the fetched data. We also use the `useState` hook to manage the loading state of our component.

We can now use the `useApi` hook in our app:

```jsx
import React from "react";
import useApi from "./useApi";

const App = () => {
  const [data, loading] = useApi("https://jsonplaceholder.typicode.com/todos/1");

  if (loading) {
    return <div>Loading...</div>;
  }

  return <div>{data.title}</div>;
};

export default App;
```



