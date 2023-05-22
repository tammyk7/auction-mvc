import { render, fireEvent, getByText } from '@testing-library/react';
import QuoteApp from '../App';
import "@testing-library/jest-dom";

describe('QuoteApp', () => {
  it('does not have the hard coded quote', () => {
    const { getByTestId } = render(<QuoteApp />);
    expect(getByTestId('quote-text')).not.toHaveTextContent('Quote text here.');
    expect(getByTestId('quote-author')).not.toHaveTextContent('- Author -');
  });

  // HOW TO TEST?
  // it('generates a new quote when the New Quote button is pressed', () => {
  //   const { getByText, getByTestId } = render(<QuoteApp />);
  //   fireEvent.click(getByText('New Quote'));
  //   expect(getByTestId('quote-text')).toHa
  // })
});

