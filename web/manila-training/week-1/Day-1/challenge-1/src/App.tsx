import React from 'react'
import './App.css'

// Use this API to fetch quotes ---> 'https://type.fit/api/quotes'

const QuoteApp: React.FC = () => {
  return (
    <div>
      <h1>Quote of the Day</h1>
      <div>
        <h2 data-testid="quote-text">Genius is one percent inspiration and ninety-nine percent perspiration.</h2>
        <h3 data-testid="quote-author">- Thomas Edison -</h3>
        <button onClick={() => console.log('generate new quote')}>New Quote</button>
      </div>
    </div>
  );
};

export default QuoteApp;