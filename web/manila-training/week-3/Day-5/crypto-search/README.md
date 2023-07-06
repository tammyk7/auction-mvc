### Real-Time Search with Autocomplete for Crypto Stocks

In this challenge, you will create a real-time search component with autocomplete functionality for crypto stocks. The component should subscribe to a `search$` Observable that emits search results. Use `bind` from react-rxjs to create a custom hook that subscribes to the `search$` Observable.

You will be using the Coinranking API to fetch search suggestions:

```js
https://api.coinranking.com/v2/search-suggestions?query={query}
```

Go to https://developers.coinranking.com/api/documentation and read through getting started to sign up for a free account and create an API key.

Here's a skeleton to get you started:

```jsx
import { bind, createSignal } from '@react-rxjs/core';
import { EMPTY, of } from 'rxjs';
import { catchError, switchMap, startWith } from 'rxjs/operators';
import { FC } from 'react';

interface SearchSuggestion {
  // TODO: Define the structure of a search suggestion based on the Coinranking API
}

interface Error {
  message: string;
}

const [useSearch, searchSubject$] = createSignal<string>();

const search$ = searchSubject$.pipe(
  switchMap(query => {
    // TODO: Make an API call to fetch search suggestions
    // Use switchMap to cancel previous API calls and only keep the latest one
    // Simulate an API call
    if (Math.random() < 0.5) {
      return of<SearchSuggestion[]>([]);
    } else {
      return EMPTY;
    }
  }),
  catchError((error: Error) => of({ message: error.message })),
  startWith({ loading: true })
);

const SearchComponent: FC = () => {
  const [query, setQuery] = useSearch();
  const [results] = useSearch(query);

  if ('message' in results) {
    // TODO: Display an error message
  }

  if (results.loading) {
    // TODO: Display a loading state while the API request is in progress
  }

  // TODO: Display the search results

  return (
    <div>
      <input type="text" value={query} onChange={e => setQuery(e.target.value)} />
      {/* TODO: Display the search results */}
    </div>
  );
};
```

To run this application:
1. `cd challenge-crypto-search`
2. `npm i`
3. `npm run dev`

Validate your solution by running it against the test suite:
`npm run test`

#### Requirements:

1. Use React, TypeScript, RxJS, and react-rxjs.
2. Implement real-time search with autocomplete.
3. Use `switchMap` to optimize the API calls. When a new search query is entered, cancel the previous API call and only keep the latest one.
4. TODO: Implement error handling. If the API request fails, the Observable should emit an error.
5. TODO: Handle the error in your component and display an appropriate error message to the user.
6. TODO: Implement a loading state. While the API request is in progress, display a loading message or spinner to the user.
7. Do NOT use any other libraries or packages.

#### Bonus:

1. TODO: Make your component more interactive. For example, allow the user to select a search suggestion and display more information about the selected crypto stock.

Good luck!