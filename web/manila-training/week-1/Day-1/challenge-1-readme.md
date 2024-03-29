### Challenge: Quote of the Day App

The goal of this challenge is to finish the implementation of a _"Quote of the Day"_ app. Use the hooks `useState` and `useEffect` as needed to fetch a random quote from the API provided when the `New Quote` button is clicked.

A skeleton app has been provided to get you started. Use [this]('https://type.fit/api/quotes') API to fetch quotes.

```tsx
// App.tsx
import React from 'react'
import './App.css'

// Use this API to fetch quotes ---> 'https://type.fit/api/quotes'

const QuoteApp: React.FC = () => {
  return (
    <div>
      <h1>Quote of the Day</h1>
      <div>
        <h2 data-testid="quote-text">Quote text here.</h2>
        <h3 data-testid="quote-author">- Author -</h3>
        <button onClick={() => console.log('generate new quote')}>New Quote</button>
      </div>
    </div>
  );
};

export default QuoteApp;
```

To run this application:
1. `cd challenge-1`
2. `npm i`
3. `npm run dev`


Validate your solution by running it against the test suite:
`npm run test`