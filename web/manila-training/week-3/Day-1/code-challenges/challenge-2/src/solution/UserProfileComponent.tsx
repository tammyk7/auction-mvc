import React, { FC } from 'react';
import { bind } from '@react-rxjs/core';
import { ajax } from 'rxjs/ajax';
import { map } from 'rxjs/operators';
import { combineLatest } from 'rxjs';
import './user-profile-component.css'

interface User {
  name: string;
  email: string | null;
  avatar: string | null;
}

interface Repo {
  name: string;
  url: string;
  updatedAt: string;
}

interface GithubResponse {
  name: string;
  email: string | null;
  avatar_url: string | null;
}

interface RepoResponse {
  name: string;
  html_url: string;
  updated_at: string;
}

// Fetch user data & repo data from the GitHub API
const [useGithubUserData] = bind((username: string) =>
  combineLatest([
    ajax.getJSON<GithubResponse>(`https://api.github.com/users/${username}`).pipe(
      map((data: GithubResponse) => ({
        name: data.name,
        email: data.email,
        avatar: data.avatar_url,
      })),
    ),
    ajax.getJSON<RepoResponse[]>(`https://api.github.com/users/${username}/repos?sort=updated&direction=desc&per_page=3`).pipe(
      map((data: RepoResponse[]) =>
        data.map((repo: RepoResponse) => ({
          name: repo.name,
          url: repo.html_url,
          updatedAt: new Date(repo.updated_at).toLocaleDateString(),
        })),
      ),
    ),
  ]),
);

/* This code fetches user data and repository data from the GitHub API. It uses a combination of ajax.getJSON requests to retrieve the necessary information.

The bind function is used to bind a username parameter to the inner function that performs the API calls. This allows for easy reuse of the function with different usernames.

The combineLatest function is used to combine the results of the two ajax.getJSON requests into an array. It waits for both requests to complete before emitting the combined result.

The first ajax.getJSON request fetches user data by accessing the GitHub API endpoint for a specific user (https://api.github.com/users/${username}). The response is then mapped to an object containing the user's name, email, and avatar URL.

The second ajax.getJSON request fetches repository data for the same user. It accesses the GitHub API endpoint for user repositories (https://api.github.com/users/${username}/repos?sort=updated&direction=desc&per_page=3) and specifies sorting options for the repositories. The response is mapped to an array of objects, where each object contains the repository name, URL, and the date of the last update formatted as a localized string.

The resulting array of user and repository data is assigned to the useGithubUserData constant variable, which can be used elsewhere in the code.
*/

interface UserProfileProps {
  username: string;
}

const UserProfileComponent: FC<UserProfileProps> = ({ username }) => {
  const combinedUserData: [User, Repo[]] = useGithubUserData(username);
  const userData = combinedUserData[0];
  const repoData = combinedUserData[1];

  return (
    <div className="wrapper-container">
      <div className="user-data-container">
        <p>Name: {userData.name}</p>
        <p>Email: {userData.email || 'Email not available'}</p>
        {userData.avatar && <img src={userData.avatar} height="200" width="200" />}
      </div>
      <div className="repo-data-container">
        <h2>Recent Repositories</h2>
          {repoData.map((repo: Repo, index: number) => (
            <div className="repo-container" key={index}>
              <a href={repo.url} target="_blank" rel="noopener noreferrer">{repo.name}</a>
              <p>Last updated: {repo.updatedAt}</p>
            </div>
          ))}
      </div>
    </div>
  );
};

export default UserProfileComponent;

