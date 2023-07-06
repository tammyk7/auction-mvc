import { render, fireEvent, waitFor, screen } from '@testing-library/react';
import { of } from 'rxjs';
import { rest } from 'msw';
import { setupServer } from 'msw/node';
import React from 'react';
import SearchComponent from '../components/SearchComponent/SearchComponent';


const server = setupServer(
  rest.get('https://api.coinranking.com/v2/search-suggestions', (req, res, ctx) => {
    return res(ctx.json({ data: [
      { id: '1', name: 'Bitcoin' },
      { id: '2', name: 'Ethereum' },
    ]}));
  })
);

beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());

test('renders search input', () => {
  render(<SearchComponent />);
  const inputElement = screen.getByRole('textbox');
  expect(inputElement).toBeInTheDocument();
});

test('displays search results', async () => {
  render(<SearchComponent />);
  const inputElement = screen.getByRole('textbox');

  fireEvent.change(inputElement, { target: { value: 'bi' } });

  await waitFor(() => screen.getByText('Bitcoin'));
  await waitFor(() => screen.getByText('Ethereum'));

  expect(screen.getByText('Bitcoin')).toBeInTheDocument();
  expect(screen.getByText('Ethereum')).toBeInTheDocument();
});

test('displays error message', async () => {
  server.use(
    rest.get('https://api.coinranking.com/v2/search-suggestions', (req, res, ctx) => {
      return res(ctx.status(500));
    })
  );

  render(<SearchComponent />);
  const inputElement = screen.getByRole('textbox');

  fireEvent.change(inputElement, { target: { value: 'bi' } });

  await waitFor(() => screen.getByText('Error: Failed to fetch search suggestions'));

  expect(screen.getByText('Error: Failed to fetch search suggestions')).toBeInTheDocument();
});

test('displays loading state', () => {
  render(<SearchComponent />);
  const inputElement = screen.getByRole('textbox');

  fireEvent.change(inputElement, { target: { value: 'bi' } });

  expect(screen.getByText('Loading...')).toBeInTheDocument();
});
