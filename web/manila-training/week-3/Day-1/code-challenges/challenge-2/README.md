
### Challenge 2: User Profile Component

Create a UserProfile component that displays a user's name and email. The component should subscribe to a `user$` Observable that emits user data. Use `bind` to create a custom hook that subscribes to the `user$` Observable.

Use the github api for users to grab information and display it.

```js
https://api.github.com/users/{username}
```

Here's a skeleton to get you started:

```jsx
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

```


To run this application:
1. `cd challenge-1`
2. `npm i`
3. `npm run dev`


Validate your solution by running it against the test suite:
`npm run test`

