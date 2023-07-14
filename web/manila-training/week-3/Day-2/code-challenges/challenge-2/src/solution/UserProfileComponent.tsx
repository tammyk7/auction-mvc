import React, { FC, useEffect, useState } from 'react';
import { ajax } from 'rxjs/ajax';
import { map } from 'rxjs/operators';
import { combineLatest, of } from 'rxjs';
import './user-profile-component.css';

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

interface UserProfileProps {
  username: string;
}


const UserProfileComponent: FC<UserProfileProps> = ({ username }) => {
  const [combinedUserData, setCombinedUserData] = useState<[User, Repo[]] | null>(null);

  useEffect(() => {
    const subscription = combineLatest([
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
    ]).subscribe(data => setCombinedUserData(data));

    return () => subscription.unsubscribe();
  }, [username]);

  if (!combinedUserData) {
    return null;  // or a loading spinner
  }

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


