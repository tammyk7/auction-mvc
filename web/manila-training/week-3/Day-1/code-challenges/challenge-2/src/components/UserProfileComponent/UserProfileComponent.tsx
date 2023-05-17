
import { bind } from '@react-rxjs/core';
import { of } from 'rxjs';
import { FC } from 'react';

interface User {
  name: string;
  email: string;
}

const user$ = of<User>({
  name: 'John Doe',
  email: 'john.doe@example.com'
});

const useUser = bind(user$);

const UserProfileComponent: FC = () => {
  const user = useUser();
  return (
    <div>
      <p>Name: {user.name}</p>
      <p>Email: {user.email}</p>
    </div>
  );
}

export default UserProfileComponent;

