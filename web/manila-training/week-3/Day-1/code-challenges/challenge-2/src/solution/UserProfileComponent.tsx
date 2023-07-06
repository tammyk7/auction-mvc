import React, { FC, useEffect, useState } from 'react';
import { bind } from '@react-rxjs/core';
import { of } from 'rxjs';

// Define the User interface
interface User {
  name: string;
  email: string | null;
}

// Define the Props interface
interface Props {
  username: string;
}

// Fetch user data from the GitHub API
const fetchUserData = async (username: string): Promise<User> => {
  const response = await fetch(`https://api.github.com/users/${username}`);
  const data = await response.json();
  return {
    name: data.name,
    email: data.email,
  };
};

// Define the UserProfileComponent
const UserProfileComponent: FC<Props> = ({ username }) => {
  const [user, setUser] = useState<User>({ name: '', email: null });

  useEffect(() => {
    fetchUserData(username).then((userData) => setUser(userData));
  }, [username]);

  const user$ = of(user);
  const [useUser] = bind(user$);

  const userData = useUser();

  return (
    <div>
      <p>Name: {userData.name}</p>
      <p>Email: {userData.email || 'Email not available'}</p>
    </div>
  );
};

export default UserProfileComponent;
