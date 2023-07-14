
### Challenge 2: User Profile Component
This coding challenge involves creating a React component that fetches and displays data from the GitHub API. The component should display a user's name, email, avatar, and a list of their 3 most recent repositories.

**Note**: The UserProfileComponent is already imported into App.tsx with the prop `username` filled in with a user's GitHub username.

### Instructions

Import necessary dependencies: You will need to import the necessary functions and operators from `@react-rxjs/core`, `rxjs/ajax`, `rxjs/operators`, and `rxjs`.

- **Define the User interface:** This interface should have the properties `name`, `email`, and `avatar`. The email and avatar properties should be nullable.

- **Define the Repo interface:** This interface should have the properties `name`, `url`, and `updatedAt`.

- **Define the GithubResponse and RepoResponse interfaces:** These interfaces should match the structure of the data returned by the GitHub API.

- **Implement a single function to fetch user data & repo data from the GitHub API:**
  - This function should use the bind function from `@react-rxjs/core` and the `ajax` function from `rxjs/ajax`.
  - It should take a `username` as a parameter and **return an array containing the user data and repo data**.
  - The user data should be fetched from `https://api.github.com/users/{username}` and the repo data should be fetched from `https://api.github.com/users/{username}/repos?sort=updated&direction=desc&per_page=3`.
  - Use the `map` operator from `rxjs/operators` to transform the response data to match the User and Repo interfaces.
  - Consider how to combine multiple streams into one for better handling of the data.


- **Render the user's name, email, and avatar:** The UserProfileComponent should render the user's name and email. If the email is null, it should render 'Email not available'. If the user has an avatar, it should render an img element with the avatar URL as the src.

- **Render a list of the user's latest 3 repositories:** The UserProfileComponent should render a list of the user's repositories. For each repository, it should render the name of the repository as a link to the repository and provide the date it was last updated.

---

Here's a skeleton to get you started:

```jsx
import React, { FC } from 'react';
// TODO: Import necessary dependencies from '@react-rxjs/core', 'rxjs/ajax', 'rxjs/operators', and 'rxjs'

import './user-profile-component.css'

// TODO: Define the User interface

// TODO: Define the Repo interface

// TODO: Define the GithubResponse interface

// TODO: Define the RepoResponse interface

// TODO: Implement a function to fetch user data & repo data from the GitHub API
// Use the bind function from '@react-rxjs/core' and the ajax function from 'rxjs/ajax'
// The function should take a username as a parameter and return an array containing the user data and repo data
// The user data should be fetched from 'https://api.github.com/users/{username}'
// The repo data should be fetched from 'https://api.github.com/users/{username}/repos?sort=updated&direction=desc&per_page=3'
// Use the map operator from 'rxjs/operators' to transform the response data to match the User and Repo interfaces

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
```

---

To run this application:

1. `cd challenge-2`
2. `npm i`
3. `npm run dev`

Validate your solution by running it against the test suite:
`npm run test`

Take a look at the mock data in the test to get a better understanding of how to structure & name your data.
