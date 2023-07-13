# Week 3: Integrating RxJS with React

This week we will focus on combining your newfound RxJS knowledge with React. We will introduce you to a helper library called React-RxJS that will allow you to easily connect RxJS observables to React components using a handful of utility methods and components. We will compare this library's approach against only using RxJS, as there will be cases where you will want greater control over the composition & lifecycle of your state streams.

We will walk through several different scenarios in React applications where you will want to add reactivity: components, custom hooks, error handling, and more.

We will wrap up the week with two code challenges. The first will be to create a real-time search component using React, TypeScript, RxJS, and react-rxjs hooks. The second will be to build a small cryptocurrency watchlist application.

Below you will find an outline for each day's material. Each bullet point with link to the corresponding file. The order listed each day is the order in which you should go through the material.

** In each day's directory, start with the README file. It will outline the order of the articles for the given day or you can use the outline below.

## Day 1: Connecting RxJS to React with React-RxJS

- [Problems Using RxJS with React](Day-1/Problems-Using-RxJS-with-React.md)
- [Introduction to React-RxJS](Day-1/Introduction-to-React-RxJS.md)
- [A Guide to Bind](Day-1/A-Guide-to-Bind.md)
- [A Guide to Signals](Day-1/A-Guide-to-Signals.md)
- [Observables vs. Signals vs. Subjects](Day-1/Observables-vs-Signals-vs-Subjects.md)
- [React-RxJS Utilities](Day-1/React-RxJS-Utilities.md)

### Code challenges

- [Counter](Day-1/code-challenges/challenge-1/README.md)
- [User Profile](Day-1/code-challenges/challenge-2/README.md)

## Day 2: Connecting Observables to React without React-RxJS

- [Subjects &amp; Observables in React](Day-2/Subjects-&-Observables-in-React.md)
- [Connecting RxJS Observables to React (without react-rxjs)](Day-2/A-Guide-to-Using-Vanilla-RxJS-with-React-Hooks.md)
- [RxJS vs React-RxJS](Day-2/RxJS-vs-React-RxJS.md)

### Code challenges

> *** note that these are going to be refactors from day 1's challenges
- [Counter](Day-2/code-challenges/challenge-1/README.md)
- [User Profile](Day-2/code-challenges/challenge-2/README.md)

## Day 3: Error Handling and Higher-Order Observables

- [Error Handling with catchError, retry &amp; retryWhen](Day-3/Error-Handling-with-RxJS-Operators.md)
- [Error Handling with React-RxJS](Day-3/Error-Handling-with-React-RxJS.md)
- [Higher-Order Observables: mergeMap, switchMap &amp; concatMap](Day-3/Higher-Order-Observables.md)

## Day 4: Review Code Tutorials

- [Github Issue Tracker](https://react-rxjs.org/docs/tutorial/github-issues)
- [ToDo App](https://react-rxjs.org/docs/tutorial/todos)

## Day 5: Code Challenge: Real-time Search

- [Implement a real-time search feature with React-RxJS](Day-5/crypto-search/README.md)
