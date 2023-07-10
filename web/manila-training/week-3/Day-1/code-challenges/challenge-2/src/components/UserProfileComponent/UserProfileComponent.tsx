import React, { FC } from 'react';
// TODO: Import necessary dependencies from '@react-rxjs/core', 'rxjs/ajax', 'rxjs/operators', and 'rxjs'

import './user-profile-component.css'

// HINT: Take a look at the mock data in the test file for this component to get a better understanding of the data structures & property names needed

// TODO: Define the User interface

// TODO: Define the Repo interface

// TODO: Define the GithubResponse interface

// TODO: Define the RepoResponse interface

// TODO: Implement a single function to fetch user data & repo data from the GitHub API
  // Use the bind function from '@react-rxjs/core' and the ajax function from 'rxjs/ajax'
  // The function should take a username as a parameter and return an array containing the user data and repo data
  // The user data should be fetched from 'https://api.github.com/users/{username}'
  // The repo data should be fetched from 'https://api.github.com/users/{username}/repos?sort=updated&direction=desc&per_page=3'
  // Use the map operator from 'rxjs/operators' to transform the response data to match the User and Repo interfaces

  // This component is imported already in the App.tsx file with a placeholder username in place

interface UserProfileProps {
  username: string;
}

const UserProfileComponent: FC<UserProfileProps> = ({ username }) => {
  // TODO: Use the function you implemented to fetch the user data and repo data

  return (
    <div className="wrapper-container">
      <div className="user-data-container">
        {/* TODO: Render the user's name and email. If the email is null, render 'Email not available' */}
        {/* TODO: If the user has an avatar, render an img element with the avatar URL as the src */}
      </div>
      <div className="repo-data-container">
        <h2>Recent Repositories</h2>
          {/* TODO: Render a list of the user's repositories. For each repository, render a link to the repository and the date it was last updated */}
      </div>
    </div>
  );
};

export default UserProfileComponent;

