import { render, screen, waitFor } from '@testing-library/react';
import QuoteApp from '../App';

describe('QuoteApp', () => {
  it('does not have the hard coded quote', async () => {
    render(<QuoteApp />);

    await waitFor(() => {
      expect(screen.getByTestId("quote-text")).not.toHaveTextContent('Quote text here.');
      expect(screen.getByTestId("quote-text")).not.toHaveTextContent('- Author -')
    }, {
      timeout: 1000
    });
  });
});

