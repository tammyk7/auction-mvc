import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import { setupServer } from 'msw/node';
import { rest } from 'msw';
import UserProfileComponent from '../components/UserProfileComponent/UserProfileComponent';

const mockUserData = {
  name: 'Test User',
  email: 'test@example.com',
  avatar_url: 'https://example.com/avatar.jpg',
};

const mockRepoData = [
  {
    name: 'Test Repo 1',
    html_url: 'https://github.com/test/repo1',
    updated_at: new Date().toISOString(),
  },
  {
    name: 'Test Repo 2',
    html_url: 'https://github.com/test/repo2',
    updated_at: new Date().toISOString(),
  },
  {
    name: 'Test Repo 3',
    html_url: 'https://github.com/test/repo3',
    updated_at: new Date().toISOString(),
  },
];

const handlers = [
  rest.get('https://api.github.com/users/:username', (req, res, ctx) => {
    return res(ctx.json(mockUserData));
  }),
  rest.get('https://api.github.com/users/:username/repos', (req, res, ctx) => {
    return res(ctx.json(mockRepoData));
  }),
];

const server = setupServer(...handlers);

beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());

test("renders user's name & email, if available", async () => {
  render(<UserProfileComponent username="test" />);

  await waitFor(() => {
    expect(screen.getByText('Name: Test User')).toBeInTheDocument();
    expect(screen.getByText('Email: test@example.com')).toBeInTheDocument();
  });
});

it('renders user avatar using an img tag', async () => {
  render(<UserProfileComponent username="test" />);
  await waitFor(() => {
    const img = screen.getByRole('img');
    expect(img).toHaveAttribute('src', 'https://example.com/avatar.jpg');
  });
});

test('renders a list of 3 repositories with the repo name as a link and the date it was last updated', async () => {
  render(<UserProfileComponent username="test" />);
  await waitFor(() => {
    expect(screen.getByText('Test Repo 1')).toBeInTheDocument();
    expect(screen.getByText('Test Repo 2')).toBeInTheDocument();
    expect(screen.getByText('Test Repo 3')).toBeInTheDocument();
  });
});
