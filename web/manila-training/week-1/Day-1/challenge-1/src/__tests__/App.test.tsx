import { render } from '@testing-library/react';
import QuoteApp from '../App';
import "@testing-library/jest-dom";

describe('QuoteApp', () => {
  it('does not have the hard coded quote', () => {
    const { getByTestId } = render(<QuoteApp />);
    expect(getByTestId('quote-text')).not.toHaveTextContent('Quote text here.');
    expect(getByTestId('quote-author')).not.toHaveTextContent('- Author -');
  });
});

