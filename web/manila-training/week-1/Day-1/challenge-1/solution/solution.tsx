import React, { useState, useEffect } from 'react';
import './App.css'

interface Quote {
  text: string
  author: string
}

const QuoteApp: React.FC = () => {
  const [quote, setQuote] = useState<Quote | null>();

  const fetchQuote = async () => {
    try {
      const response = await fetch('https://type.fit/api/quotes');
      const data = await response.json();
      const randomQuote = data[Math.floor(Math.random() * data.length)];
      setQuote(randomQuote);
    } catch (error) {
      console.error('Error fetching quote:', error);
    }
  };

  const handleNewQuote = () => {
    fetchQuote();
  };

  useEffect(() => {
    fetchQuote();
  }, []);

  return (
    <div>
      <h1>Quote of the Day</h1>
      {quote && (
        <div>
          <h2 data-testid="quote-text">{quote.text}</h2>
          <h3 data-testid="quote-author">- {quote.author}</h3>
        </div>
      )}
      <button onClick={handleNewQuote}>New Quote</button>
    </div>
  );
};

export default QuoteApp;
