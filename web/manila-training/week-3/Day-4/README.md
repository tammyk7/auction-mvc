**Challenge 1: Build a Real-Time Search Feature**

Create a search bar that filters a list of items in real-time. The list can be anything you want (e.g., a list of movies, books, etc.). The key here is to use React-RxJS to handle the stream of user input events.

- Use the `createSignal` function from `@react-rxjs/utils` to create a signal for the search input.
- Use the `bind` function from `@react-rxjs/core` to create a hook that's bound to the search input signal.
- Use the `useEffect` hook in your component to subscribe to the search input signal and update the list based on the user's input.
