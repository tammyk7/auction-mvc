# Error Handling with catchError, retry & retryWhen
In the development of applications, error handling is not just a necessity, it is an art. Harnessing the power of reactive programming using RxJS in concert with React, we have a suite of robust tools that enable us to manage errors in an elegant and efficient manner. This tutorial will delve into the `catchError`, `retry`, and `retryWhen` operators, illuminating their utility in the construction of resilient applications.

## Navigating the Rapids with catchError

The `catchError` operator acts as a lifeline, catching errors from the source Observable and returning a new Observable or escalating the error. It's a powerful tool when you need to manage an error and substitute the failed Observable with a fresh one.

Consider this sample usage of `catchError`:

```tsx
import { bind } from '@react-rxjs/core';
import { ajax } from 'rxjs/ajax';
import { catchError } from 'rxjs/operators';

const [useFetchData, fetchData$] = bind(
  ajax.getJSON('<https://api.example.com/data>').pipe(
    catchError((error) => {
      console.error('Error:', error);
      return of({ data: 'Default data' });
    }),
  ),
);

function MyComponent() {
  const data = useFetchData();
  // Render your component using the data
}

```

In this illustration, the `catchError` operator intercepts any failure from the `getJSON` request, logs the error to the console, and returns an Observable that emits a default data object.

## Repetition as a Strategy with retry

The `retry` operator provides a straightforward mechanism to resubscribe to the source Observable if it encounters an error. This operator proves useful when an error could be transient, such as an intermittent network issue, and retrying the operation may lead to success.

The following is an example of `retry` in action:

```tsx
import { bind } from '@react-rxjs/core';
import { ajax } from 'rxjs/ajax';
import { catchError, retry } from 'rxjs/operators';

const [useFetchData, fetchData$] = bind(
  ajax.getJSON('<https://api.example.com/data>').pipe(
    retry(3),
    catchError((error) => {
      console.error('Error:', error);
      return of({ data: 'Default data' });
    }),
  ),
);

function MyComponent() {
  const data = useFetchData();
  // Render your component using the data
}

```

In this scenario, if the `getJSON` request fails, the `retry` operator automatically resubscribes to the source Observable, repeating this process up to 3 times. If the request continues to fail after 3 attempts, the `catchError` operator steps in, logging the error and returning a default data object.

## Intelligent Repetition with retryWhen

The `retryWhen` operator can be thought of as a more flexible and intelligent sibling of `retry`. It allows you to determine when to retry the source Observable based on the error that was thrown. This gives you the power to implement complex retry strategies, such as exponential backoff.

Here's a demonstration of the `retry

`When` operator:

```tsx
import { bind } from '@react-rxjs/core';
import { ajax } from 'rxjs/ajax';
import { catchError, delay, retryWhen, take } from 'rxjs/operators';

const [useFetchData, fetchData$] = bind(
  ajax.getJSON('<https://api.example.com/data>').pipe(
    retryWhen((errors) =>
      errors.pipe(
        delay(2000),
        take(10)
      )
    ),
    catchError((error) => {
      console.error('Error:', error);
      return of({ data: 'Default data' });
    }),
  ),
);

function MyComponent() {
  const data = useFetchData();
  // Render your component using the data
}

```

In this example, if the `getJSON` request fails, the `retryWhen` operator activates and waits for 2 seconds before resubscribing to the source Observable. This process repeats up to 10 times. If the request continues to fail after 10 attempts, the `catchError` operator steps in, logs the error, and returns a default data object.

## Interaction with React Error Boundaries

React error boundaries provide a powerful mechanism to handle errors in React components. They catch errors during rendering and in lifecycle methods and display a fallback UI. However, these error boundaries don't catch errors from asynchronous operations or errors outside React components such as network requests.

When using RxJS operators in a React component, it's important to connect the error handling mechanisms of both libraries. One way to do this is to throw the error from the RxJS Observable in a way that the error boundary can catch it.

For instance, you can handle the error from the RxJS Observable inside a `useEffect` hook and then throw it inside the component rendering, which can be caught by the error boundary:

```tsx
import { useEffect, useState } from 'react';
import { useFetchData } from './dataService';

function MyComponent() {
  const [error, setError] = useState(null);
  const data = useFetchData({
    onError: (err) => {
      setError(err);
    },
  });

  if (error) {
    throw error;
  }

  // Render your component using the data
}

```

In this scenario, the `useFetchData` hook receives an `onError` callback that sets the error state when an error occurs in the Observable. If there's an error, it is thrown during the component rendering and can be caught by a surrounding error boundary.

In summary, error handling in RxJS is a powerful feature that can help you build robust React applications. With the `catchError`, `retry`, and `retryWhen` operators, you can handle errors gracefully and provide a better user experience.