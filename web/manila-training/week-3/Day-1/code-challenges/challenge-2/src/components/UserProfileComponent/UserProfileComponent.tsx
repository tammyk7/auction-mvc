import React, { FC } from 'react';
import { of } from 'rxjs';
import { switchMap, map, catchError } from 'rxjs/operators';
import { bind } from '@react-rxjs/core';

// Define the User interface
interface User {
  name: string;
  email: string | null;
  avatar: string | null;
}

// Define the Repo interface
interface Repo {
  name: string;
  updated_at: string;
}

// Define the Props interface
interface Props {
  usernames: string[];
}

// Define the UserProfileComponent
const UserProfileComponent: FC<Props> = ({ usernames }) => {
  const [useUsers, users$] = bind(
    of(usernames).pipe(
      switchMap((usernames) =>
        Promise.all(
          usernames.map((username) =>
            fetch(`https://api.github.com/users/${username}`)
              .then(response => response.json())
              .then(data => ({
                name: data.name,
                email: data.email,
                avatar: data.avatar_url,
              })),
          ),
        ),
      ),
      catchError(() => []),
    ),
  );

  const [useRepos, repos$] = bind(
    of(usernames).pipe(
      switchMap((usernames) =>
        Promise.all(
          usernames.map((username) =>
            fetch(`https://api.github.com/users/${username}/repos?sort=updated`)
              .then(response => response.json())
              .then(data => data.slice(0, 3)),
          ),
        ),
      ),
      catchError(() => []),
    ),
  );

  const users = useUsers();
  const repos = useRepos();

  return (
    <div>
      {users?.map((user: User, userIndex: number) => (
        <div key={userIndex}>
          <p>Name: {user.name}</p>
          <p>Email: {user.email || 'Email not available'}</p>
          {user.avatar && <img src={user.avatar || ''} height="200" width="200" /> }
          <h2>Recent Repositories</h2>
          {repos[userIndex]?.map((repo: Repo, repoIndex: number) => (
            <div key={repoIndex}>
              <p>Repo Name: {repo.name}</p>
              <p>Last Updated: {new Date(repo.updated_at).toLocaleDateString()}</p>
            </div>
          ))}
        </div>
      ))}
    </div>
  );
};

export default UserProfileComponent;
