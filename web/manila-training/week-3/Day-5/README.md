# Technical Specification: Reactive Todo App using React, RxJS, and React-RxJS Hooks with TypeScript

## Overview

The goal is to create a reactive Todo application using React, RxJS, and React-RxJS hooks with TypeScript. The application will allow users to add, delete, and toggle the status of todo items.

## Tech Stack

- **React**: A JavaScript library for building user interfaces.
- **RxJS**: A library for reactive programming using Observables.
- **React-RxJS**: A library that combines React and RxJS to handle state management in a reactive way.
- **TypeScript**: A typed superset of JavaScript that adds static types.

## Key Features

1. **Add Todo**: Users can add a new todo item to the list.
2. **Delete Todo**: Users can delete a todo item from the list.
3. **Toggle Todo**: Users can toggle the status of a todo item between completed and uncompleted.

## Components

- **TodoInput**: This component will contain an input field and a button to add new todo items.
- **TodoItem**: This component will represent a single todo item in the list. It will display the todo text and have buttons to toggle the todo status and delete the todo.
- **TodoList**: This component will display a list of TodoItem components.

## State Management

We will use React-RxJS to manage the state of the todo list. The state will be an array of todo items, where each item is an object with the following structure:

```typescript
type Todo = {
  id: string;
  text: string;
  completed: boolean;
};
```

We will use the `createSignal` function from `@react-rxjs/utils` to create signals for adding, deleting, and toggling todos. We will use the `bind` function from `@react-rxjs/core` to create hooks that are bound to these signals.

## Code Structure

1. **TodoInput Component**: This component will use the `setText` function from the `textChange$` signal to update the text of the new todo item.

```typescript
const [textChange$, setText] = createSignal<string>();
```

2. **TodoItem Component**: This component will use the `toggleTodo` and `deleteTodo` functions from the respective signals to update the status of a todo item and delete a todo item.

```typescript
const [toggleTodo$] = createSignal<string>();
const [deleteTodo$] = createSignal<string>();
```

3. **TodoList Component**: This component will use the `useTodoList` hook to subscribe to the todo list state and re-render whenever the state changes.

```typescript
const [useTodoList] = bind(todoList$);
```

4. **State Management**: We will use the `scan` operator from `rxjs/operators` to handle the state transitions based on the signals.

```typescript
const todoList$ = merge(
  textChange$.pipe(map(text => ({ type: 'ADD_TODO', text }))),
  toggleTodo$.pipe(map(id => ({ type: 'TOGGLE_TODO', id }))),
  deleteTodo$.pipe(map(id => ({ type: 'DELETE_TODO', id })))
).pipe(
  scan((todos: Todo[], action) => {
    switch (action.type) {
      case 'ADD_TODO':
        return [...todos, { id: uuid(), text: action.text, completed: false }];
      case 'TOGGLE_TODO':
        return todos.map(todo => todo.id === action.id ? { ...todo, completed: !todo.completed } : todo);
      case 'DELETE_TODO':
        return todos.filter(todo => todo.id !== action.id);
      default:
        return todos;
    }
 

}, []),
  startWith([])
);
```




## Additional Enhancements

- **Edit Todo**: Allow users to edit the text of a todo item.
- **Persist Todos**: Persist the todo items in local storage so they are not lost when the page is refreshed.
- **Filter Todos**: Allow users to filter the todo list by status (all, active, completed).