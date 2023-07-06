
import { EMPTY, of } from 'rxjs';
import { catchError, switchMap, startWith } from 'rxjs/operators';
import React, { FC } from 'react';
import { createSignal, bind } from '@react-rxjs/utils';

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

export default SearchComponent;