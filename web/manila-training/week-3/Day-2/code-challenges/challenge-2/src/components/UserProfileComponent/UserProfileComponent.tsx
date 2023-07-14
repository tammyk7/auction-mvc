
import React, { FC, useEffect } from 'react';

*// TODO: Import necessary dependencies from 'rxjs/ajax', 'rxjs/operators', and 'rxjs'*

import './user-profile-component.css'

*// TODO: Define the User interface*

*// TODO: Define the Repo interface*

*// TODO: Define the GithubResponse interface*

*// TODO: Define the RepoResponse interface*

*// TODO: Using the ajax operator from RxJS, get the user data and repo data and combine them into a single stream. Manually connect & disconnect to this stream inside the component.*

*// The user data should be fetched from '<https://api.github.com/users/{username}>'*

*// The repo data should be fetched from '<https://api.github.com/users/{username}/repos?sort=updated&direction=desc&per_page=3>'*

*// Use the map operator from 'rxjs/operators' to transform the response data to match the User and Repo interfaces*

interface UserProfileProps {
  username: string;
}

const UserProfileComponent: FC<UserProfileProps> = ({ username }) => {

  useEffect(() => {
    // TODO: Subscribe to the combined stream created to fetch the user data and repo data*
  }, [])

  return (

    <div className="wrapper-container">

      <div className="user-data-container">

        {*/* TODO: Render the user's name and email. If the email is null, render 'Email not available' */*}

        {*/* TODO: If the user has an avatar, render an img element with the avatar URL as the src */*}

      </div>

      <div className="repo-data-container">

        <h2>Recent Repositories</h2>

        {*/* TODO: Render a list of the user's repositories. For each repository, render a link to the repository and the date it was last updated */*}

      </div>

    </div>

  );

};

export default UserProfileComponent;
