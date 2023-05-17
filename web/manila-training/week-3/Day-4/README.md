**Challenge 1: Build a Real-Time Search Feature**

Create a search bar that filters a list of items in real-time. The list can be anything you want (e.g., a list of movies, books, etc.). The key here is to use React-RxJS to handle the stream of user input events.

- Use the `createSignal` function from `@react-rxjs/utils` to create a signal for the search input.
- Use the `bind` function from `@react-rxjs/core` to create a hook that's bound to the search input signal.
- Use the `useEffect` hook in your component to subscribe to the search input signal and update the list based on the user's input.

**Challenge 2: Implement a Click Counter**

Create a button that, when clicked, increments a counter. The counter should be implemented using React-RxJS.

- Use the `createSignal` function from `@react-rxjs/utils` to create a signal for the button click event.
- Use the `bind` function from `@react-rxjs/core` to create a hook that's bound to the button click signal.
- Use the `scan` operator from `rxjs/operators` to accumulate the number of clicks.
- Use the `useEffect` hook in your component to subscribe to the button click signal and update the counter each time the button is clicked.

Remember to handle subscriptions properly to prevent memory leaks. You can use the `Subscribe` component from `@react-rxjs/core` to automatically handle subscriptions.