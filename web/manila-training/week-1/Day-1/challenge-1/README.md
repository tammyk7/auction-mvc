### Challenge: Quote of the Day App

The goal of this challenge is to finish the implementation of a _"Quote of the Day"_ app. Use the hooks `useState` and `useEffect` as needed to fetch a random quote from the API provided when the `New Quote` button is clicked.

A skeleton app has been provided to get you started.

```tsx
// App.tsx
import React from 'react'
import './App.css'

const QuoteApp: React.FC = () => {
  const fetchQuote = async () => {
    try {
      const response = await fetch('https://type.fit/api/quotes');
      const data = await response.json();
      console.log('Response data:', data);
    } catch (error) {
      console.error('Error fetching quote:', error);
    }
  };
  
  return (
    <div>
      <h1>Quote of the Day</h1>
      <div>
        <h2>Genius is one percent inspiration and ninety-nine percent perspiration.</h2>
        <h3>- Thomas Edison -</h3>
        <button onClick={() => console.log('generate new quote')}>New Quote</button>
      </div>
    </div>
  );
}

export default QuoteApp;
```

To run this application:
1. `cd challenge-1`
2. `npm i`
3. `npm run dev`


Validate your solution by running it against the test suite:
`npm run test`