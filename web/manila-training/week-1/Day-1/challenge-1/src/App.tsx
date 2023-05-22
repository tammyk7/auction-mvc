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
        <h2 data-testid="quote-text">Quote text here.</h2>
        <h3 data-testid="quote-author">- Author -</h3>
        <button onClick={() => console.log('Generate a new quote')}>New Quote</button>
      </div>
    </div>
  );
}

export default QuoteApp;
