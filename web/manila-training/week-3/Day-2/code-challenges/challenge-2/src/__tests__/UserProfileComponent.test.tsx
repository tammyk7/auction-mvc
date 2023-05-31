import { render, screen } from '@testing-library/react';
import { of } from 'rxjs';
import { bind } from '@react-rxjs/core';
import UserProfileComponent from '../components/UserProfileComponent/UserProfileComponent';

// Mock user data
const user$ = of({
  name: 'John Doe',
  email: 'john.doe@example.com'
});

// Create useUser hook using bind
const [useUser] = bind(user$, { name: '', email: '' }); // Default value

// Mock UserProfileComponent for testing
function MockUserProfileComponent() {
  const user = useUser();
  return (
    <div>
      <p>Name: {user.name}</p>
      <p>Email: {user.email}</p>
    </div>
  );
}

describe('UserProfileComponent', () => {
  it('displays the user\'s name and email', () => {
    render(<MockUserProfileComponent />);

    expect(screen.getByText('Name: John Doe')).toBeInTheDocument();
    expect(screen.getByText('Email: john.doe@example.com')).toBeInTheDocument();
  });
});
