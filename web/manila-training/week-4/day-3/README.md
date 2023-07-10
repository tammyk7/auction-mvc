# Week 4 Day 3: Error Boundaries

## Handling errors in React with Error Boundaries

Errors are an inevitable part of development so we always make sure to add error handling to make sure the users will have a better experience.

In regular JavaScript, the `try…catch` statement offers an API with which we can try and gracefully catch potentially error-prone code:

```js
try {
  console.log(p)
} catch (error) {
  console.log(`Error: ${error}`) // ReferenceError: p is not defined
}
```

Any error detected in the try block is thrown as an exception and caught in the catch block, keeping our applications more resilient to errors. In React, this construct works fine for handling JavaScript errors as below, where we’re fetching data with useEffect:

```js
useEffect(() => {
  try {
    fetchUsers()
  } catch (error) {
    setError(error)
  }
}, [])
```

But this doesn’t work so well in React components. This is because the `try...catch` statement only works for imperative code, as opposed to the declarative code we have in components.

![React Error Image](../images/error-message.png)

The error screens like the one above that we see will only show up in development. In production, the UI will be corrupted, and the user will be served a blank screen.

How can we solve for this? Enter Error Boundaries.

<hr>

## Using Error Boundaries in React

Error boundaries are React components that offer a way to handle JavaScript errors in React components. With them, we can catch JavaScript runtime errors in our components, act on those errors, and display a fallback UI.

Error boundaries operate like the `catch` block in the JavaScript `try...catch` statement with a couple exceptions: they are declarative and only catch errors in React components.

They catch errors in their child component(s), during the rendering phase, and in lifecycle methods. Errors thrown in any of the child components of an error boundary will be delegated to the closest Error Boundary in the component tree.

In contrast, Error Boundaries do not catch errors for, or that occur in:

- Event handlers
- Asynchronous code
- Server-side rendering
- Errors thrown in the error boundary itself

There are a few rules to follow for a component to act as an Error Boundary:

- It must be a class component
- It must define one or both of the lifecycle methods `static getDerivedStateFromError()` or `componentDidCatch()`

```tsx
import React, { Component, ErrorInfo, ReactNode } from 'react'

interface Props {
  children?: ReactNode
  fallback?: ReactNode
}

interface State {
  hasError: boolean
  error: Error | null
}

class ErrorBoundary extends Component<Props, State> {
  state: State = {
    hasError: false,
  }

  static getDerivedStateFromError(error: Error): State {
    // Update state so the next render will show the fallback UI.
    return { hasError: true, error }
  }

  componentDidCatch(error: Error, errorInfo: ErrorInfo) {
    // Log error to an error reporting servic
    console.error('Uncaught error:', error, errorInfo)
  }

  render() {
    if (this.state.hasError) {
      if (this.state.fallback) {
        // You can render any custom fallback UI
      }
      return <h1>Sorry.. there was an error</h1>
    }

    return this.props.children
  }
}

export default ErrorBoundary
```

In this example `ErrorBoundary` component we first define the interfaces for both the Props & State.

The `ErrorBoundary` has two optional Props:

- `children`: because we will use the `ErrorBoundary` as a HOC to wrap children components, this gives us access to those components. If you take a look in the `render()` method, we will render the children if there is no error state.
- `fallback`: this prop is not mandatory, but allows us to pass a fallback UI component as a prop to our `ErrorBoundary` HOC that will be rendered instead of the default error UI.

The rule of thumb is that `static getDerivedStateFromError()` should be used to render a fallback UI after an error has been thrown, while `componentDidCatch()` should be used to log those error(s).

`static getDerivedStateFromError()` is a lifecycle method that allows the Error Boundary a chance to update the state and thus triggering a last `render()`. In the above code snippet, the state is used to reveal a human-friendly error message instead of the broken component (e.g., this.props.children).

`componentDidCatch()` is a lifecycle method designed for triggering side-effects (e.g., logging the error to tools). You can access info.componentStack to get a developer-friendly stack trace that will be useful for triaging the bug.

Then you can wrap a part of your component tree with it:

```jsx
<ErrorBoundary fallback={<p>Something went wrong</p>}>
  <Counter />
</ErrorBoundary>
```

If `Counter` or its child component throws an error, `ErrorBoundary` will “catch” that error, display a fallback UI with the error message you’ve provided, and send a production error report to your error reporting service.

<hr>

## Where to place Error Boundaries in the component tree

Error boundaries are special React components and should be used to catch errors only where appropriate. Different error boundaries can be used in different parts of an application to handle contextual errors, though they can be generic — for example, a network connection error boundary.

You don’t need to wrap every component into a separate error boundary. When you think about the granularity of error boundaries, consider where it makes sense to display an error message. For example, in a messaging app, it makes sense to place an error boundary around the list of conversations. It also makes sense to place one around every individual message. However, it wouldn’t make sense to place a boundary around every avatar.

Errors can be tricky, dynamic, and non-deterministic. Data fetching tools like SWR or React Query, or the react-error-boundary package, can help alleviate these kinds of problems.

With the react-error-boundary package specifically, you can reduce the amount of redundant code and uncertainty you encounter with a well-tested abstraction — which, [as Kent Dodds puts it](https://kentcdodds.com/blog/use-react-error-boundary-to-handle-errors-in-react), is the last error boundary component anyone needs.

<hr>

## Error Boundaries with RxJS Observables and React-RxJS

React-RxJS is mindful of Error Boundaries, in a way that if one of the streams emits an error, the components that are subscribed to that stream will propagate that error to the nearest Error Boundary.

When a rxjs stream emits an error, the stream gets immediately closed. This way, if our strategy to recover from the error is to try again, when our Subscribe boundary resubscribes to the stream it will create a new subscription and start over again.

Head over to the [React-RxJS Documentation](https://react-rxjs.org/docs/core-concepts#error-boundaries) Error boundaries section to see an example of this in action.

You can also review more here [Error Handling with React-RxJS](../../week-3/Day-3/Error-Handling-with-React-RxJS.md)
