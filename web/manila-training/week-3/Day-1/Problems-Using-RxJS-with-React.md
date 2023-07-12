# Problems Using RxJS with React

Now that we know the core principle of working with React via functional programming and reactive programming via RxJS it is time to learn how to make the two play nicely with one another. Before we dive into specific syntaxes and scenarios, let’s go over why it is difficult to use RxJS with React.

---

## Why is it difficult to use RxJS with React?

1. **Understanding Observables:** The learning curve of RxJS is steep. The concept of Observables and operators can be difficult to grasp, especially for developers who are new to reactive programming. There's also the difficulty of understanding how to properly combine Observables with React's component lifecycle.
2. **State Management:** Integrating Observables with React's state can be tricky. It's not always clear how to best organize state between what's in the Observable and what's in React's local component state.
3. **Subscriptions and Cleanup:** Developers must remember to unsubscribe from Observables to prevent memory leaks. This can be more challenging in functional components because they don’t have lifecycle methods like **`componentWillUnmount()`**.
4. **Synchronization with React's Lifecycle:** React controls the rendering cycle, and RxJS controls the data flow. These two cycles can sometimes get out of sync, leading to unexpected behavior. For example, if an Observable emits a value that changes state after the component has unmounted, it can lead to errors.
5. **Testing:** Writing tests for components that heavily use RxJS can be complex. Tools like marble testing can help, but there's still a learning curve to understanding how to write these tests effectively.
6. **Optimization:** React uses a diffing algorithm for re-rendering components efficiently. However, when using Observables, it might trigger unnecessary re-renders if not used carefully with React's optimization techniques like **`shouldComponentUpdate`**, **`React.memo`**, **`useMemo`**, etc.

---

Okay all those things sound valid but what does that mean for us? If it’s so hard to work with RxJS together why are we learning all of this? Answer: **we use the  `react-rxjs` to overcome many of the problems listed above.** Below, let’s dig in deeper to some of the problems that pop up when using streams in React.

---

## Issues with Streams

RxJS streams are used to represent events or changing values over time and **don't execute the effect until someone subscribes to it**. Streams are **unicast: A new subscription is created for every new observer**.

This means, if an observable is passed around a React application, each component that subscribes would be creating an entirely new subscription. Not only is this expensive but it means that each new subscription created is not in sync with the others. It is possible that a component subscribes after another and misses certain values. In the case of an observable making a service call, each new subscription in a React component would cause the service to be called repeatedly.

In RxJS, streams are considered state, whereas with React state is managed internally through hooks. If a stream has multiple subscriptions, it means multiple states would be created in React. Why would we want this if multiple component all need access to the same values? Short answer: we don’t. **We want a way to share streams between components in a way that doesn’t cause new subscriptions, doesn’t cause unnecessary additional service calls, and that stays in sync across all components.**

RxJS gives us an operator called `share` that resolves many of the issues presented above.

```jsx
import { interval } from "rxjs"
import { take, share } from "rxjs/operators"

const first5SpacedNumbers = interval(1000).pipe(take(5), share())

first5SpacedNumbers.subscribe((v) => console.log("A", v))
// Will start logging A1... A2...

setTimeout(() => {
  first5SpacedNumbers.subscribe((v) => console.log("B", v))
}, 2000)
// Will continue with A3 B3... A4 B4...
```

`share` ***multicasts*** the stream, so that it **only makes one subscription to the source, and will propagate every change to all the subscriptions of the shared stream**. However, if a component subscribed later than another, it is still possible to miss some of the initial values emitted by the stream.

This is why `share` does not fully resolve the issues of using streams as sharable state in React. As React is *pull*-based, it needs access to the latest value emitted from the stream when it needs to re-render. With the current model, **it would have to wait until a new change is emitted in the stream before it can receive the new state.**

If we try to use `shareReplay`, a RxJS operator that keeps track of previous values emitted by a shared stream, we face when the source completes it will keep the last values in memory indefinitely, which would cause a **possible memory leak.**

---

Okay, all I’m hearing is problem with this, issue with that and you mentioned `react-rxjs` but have only been talking about React and RxJS.

---

React-RxJS provides us with the utility `shareLatest`.  It addresses the issue of **sharing the state between many components and keeping always the latest value**, but without the additional  memory leak issues that `shareReplay` causes.

>
> 💡 `shareLatest` is used under the hood with many of the core utilities and helper functions React-RxJS gives us. `bind` , in particular, uses `shareLatest` and adds a few extra goodies:
>
>- Leveraging Suspense, so that you can represent loading states from >the streams.
>- Leveraging Error Boundaries to allow graceful error recovery.
>- Performance optimizations, making sure React doesn't update when >it doesn't need to.
>- Manages a cache of parametric observables (when using the factory >overload).



Finally! Some solutions to our problems. Let’s step back and go high level again.

# React-RxJS’s true goal is to act as a seamless bridge between React & RxJS.

**Here are the main ways React-RxJS simplifies RxJS usage in React:**

1. **Providing Reactive Components**: **`react-rxjs`** provides a set of hooks like **`useObservable`**, **`bind`**, and **`connectObservable`** that you can use in your functional components. These hooks allow you to subscribe to Observables and automatically update your components whenever the Observable emits a new value.
2. **Reducing Boilerplate**: Without **`react-rxjs`**, managing subscriptions and ensuring they are cleaned up when a component unmounts can result in a lot of repetitive code. **`react-rxjs`** handles this for you, reducing the boilerplate needed in your components.
3. **Simplifying State Management**: You can use RxJS observables as a store for your application's state. **`react-rxjs`** helps in connecting this store with your React components, allowing for a clear, unidirectional data flow.
4. **Making Error Handling Easier**: If an error occurs in an Observable stream, **`react-rxjs`** makes it easier to catch and handle these errors, so they do not cause your entire application to crash.
5. **Enhancing Performance**: Since **`react-rxjs`** uses RxJS, it also benefits from features like operators that allow you to control when and how updates are triggered, helping to prevent unnecessary renders.

---

Now you know why Adaptive uses React-RxJS (with RxJS) to build our React applications. Next we will go into the core concept to understand to best leverage React-RxJS to build declarative, reactive programs.

>
> 💡 It is important to keep in mind that while React-RxJS helps us resolve many of the issues brought up by vanilla RxJs and React, there will be occasions where we might use plain RxJS instead of with the helper library. **React-RxJS is meant to be used with RxJS.**

---
### **Further Reading**

[Core Concepts | React-RxJS](https://react-rxjs.org/docs/core-concepts)